package edu.whut.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import edu.whut.domain.temp.Dot;
import edu.whut.pojo.DotInfo;
import edu.whut.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test")
@Slf4j
public class TestController {
    @Autowired
    private ResourceLoader resourceLoader;
    /**
     * 此处测试运行python程序
     */
    @GetMapping("python")
    public void invokePython(){
        String[] arguments=new String[]{"/Users/wunder/PycharmProjects/testPython/vene/bin/python3.12",
                "/Users/wunder/PycharmProjects/testPython/08-plus.py"};
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(arguments);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            //输出结果
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            //waitFor是用来显示脚本是否运行成功，1表示失败，0表示成功，还有其他的表示其他错误
            int re = proc.waitFor();
            System.out.println(re);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("excel")
    public Result getExcelData() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:dots.xlsx");
        File file = resource.getFile();
        //ExcelReaderUtils.getDotInfoList(file.getAbsolutePath());
        /*List<DotInfo> dotInfoList =
                ExcelReaderUtils.getDotInfoList(file.getAbsolutePath());*/
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

        return Result.success(dots);
    }

}
