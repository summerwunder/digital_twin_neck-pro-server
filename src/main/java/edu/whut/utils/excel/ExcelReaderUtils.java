package edu.whut.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.whut.config.websocket.WebSocketExcelServer;
import edu.whut.domain.temp.Dot;
import edu.whut.pojo.DotInfo;
import edu.whut.utils.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 获取Excel表格的工具
 */
@Slf4j
@Component
public class ExcelReaderUtils {
    @Autowired
    private  ResourceLoader resourceLoader;
    /**
     * 仅限获取DotInfo类的数据
     * TODO 需要修改多线程和异步的逻辑
     */
    public static List<DotInfo> getDotInfoList(String file) {
        List<DotInfo> dotInfos = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(4); // 使用固定大小的线程池

        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowIndex = sheet.getLastRowNum();

            // 计算每个子任务处理的行数
            int batchSize = 10000;
            int numThreads = (int) Math.ceil((double) (lastRowIndex - 1) / batchSize);

            // 创建任务列表
            List<Callable<List<DotInfo>>> tasks = new ArrayList<>();
            for (int i = 0; i < numThreads; i++) {
                final int startRow = 1 + i * batchSize;
                final int endRow = Math.min(startRow + batchSize - 1, lastRowIndex);

                tasks.add(() -> {
                    List<DotInfo> resultList = new ArrayList<>();
                    for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
                        Row row = sheet.getRow(rowIndex);
                        DotInfo dot = new DotInfo();
                        dot.setX(row.getCell(1).getNumericCellValue());
                        dot.setY(row.getCell(2).getNumericCellValue());
                        dot.setZ(row.getCell(3).getNumericCellValue());
                        dot.setData(row.getCell(4).getNumericCellValue());
                        resultList.add(dot);
                    }
                    return resultList;
                });
            }

            // 提交任务并获取结果
            List<Future<List<DotInfo>>> results = executor.invokeAll(tasks);
            for (Future<List<DotInfo>> future : results) {
                dotInfos.addAll(future.get());
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown(); // 关闭线程池
        }
        log.info("dotinfos(size)---->{}",dotInfos.size());

        return dotInfos;
    }

    /**
     * 重载读取点云数据的函数，使用EasyExcel，性能更好
     * @param file
     * @return
     */
    public static List<DotInfo> getDotInfoList(File file){
        List<DotInfo> dots=new ArrayList<>();
        EasyExcel.read(file.getAbsolutePath(), Dot.class, new AnalysisEventListener<Dot>() {
            @Override
            public void invoke(Dot dot, AnalysisContext analysisContext) {
                DotInfo dotInfo = new DotInfo();
                BeanUtils.copyProperties(dot,dotInfo);
                dots.add(dotInfo);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                log.info("{}","加载完毕");
            }
        }).sheet(0).doRead();
        log.info("dots(size)----{}",dots.size());
        return dots;
    }
}
