package com.example.controller;




import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author booty
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping( "/getPort")
    public String getPort(){
        return "springCloud with nacos paymentPort："+serverPort+" >>>> UUID= "+ UUID.randomUUID().toString();
    }


}

