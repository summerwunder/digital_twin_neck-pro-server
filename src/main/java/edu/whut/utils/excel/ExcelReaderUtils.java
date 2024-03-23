package edu.whut.utils.excel;

import edu.whut.pojo.DotInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 获取Excel表格的工具
 */
@Slf4j
public class ExcelReaderUtils {
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

}
