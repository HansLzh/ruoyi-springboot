package com.ruoyi.jenkins.common;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@RestController
public class JenkinsController {
    private static final Logger log = LoggerFactory.getLogger(JenkinsController.class);

    @Autowired
    private ServerConfig serverConfig;

    /**
     * gitlab webhook 请求转发 jenkins
     */
    @PostMapping("jenkins/run")
    public AjaxResult fileDownload(@RequestParam String token, @RequestBody JSONObject body, HttpServletResponse response, HttpServletRequest request) {
        try {
            JSONObject sender = body.getObject("sender", JSONObject.class);
            String login = sender.getString("login"); //HansLzh
            String id = sender.getString("id"); //39511365
            // 验证是否自己的gitlab发过来的请求
            if ("HansLzh".equals(login) && "39511365".equals(id)) {
                // 请求jenkins，开始构建
                String sendGet = null;
                switch (token) {
                    case "ruoyi-springboot":
                        sendGet = HttpUtils.sendGet("http://122.51.106.108:8081/job/" +
                                "%E3%80%90RuoYi-Springboot%E3%80%91build/build", "token=" + token);
                        break; //可选
                    case "ruoyi-vue":
                        sendGet = HttpUtils.sendGet("http://122.51.106.108:8081/job/" +
                                "%E3%80%90RuoYi-Vue%E3%80%91build/build", "token=" + token);
                        break; //可选
                }
                System.out.println(sendGet);
                return AjaxResult.success(sendGet);
            }
        } catch (Exception e) {
            log.error("构建api转发失败", e);
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success("失败");
    }

}
