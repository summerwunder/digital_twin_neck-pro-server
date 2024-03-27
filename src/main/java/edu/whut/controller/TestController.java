package edu.whut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.whut.config.websocket.WebSocketExcelServer;
import edu.whut.pojo.DotInfo;
import edu.whut.response.Result;
import edu.whut.utils.excel.ExcelReaderUtils;
import edu.whut.utils.security.SecurityUtil;
import jakarta.websocket.Session;
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
import java.util.List;

@RestController
@RequestMapping("test")
@Slf4j
public class TestController {

    /**
     * 此处测试运行python程序
     */

}
