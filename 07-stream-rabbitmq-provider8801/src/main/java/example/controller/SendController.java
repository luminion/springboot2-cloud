package example.controller;

import example.service.IMessageProviderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author booty
 * @version 1.0
 */
@RestController
public class SendController {
    @Resource
    IMessageProviderService providerService;

    @GetMapping("send/{msg}")
    String send(@PathVariable("msg") String msg) {
        providerService.send(msg);
        return "success";
    }


}
