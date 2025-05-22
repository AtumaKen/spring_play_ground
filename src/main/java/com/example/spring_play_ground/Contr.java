package com.example.spring_play_ground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("play")
public class Contr {

    @Autowired
    public  ContService contService;

    @GetMapping("{rrn}")
    public Map<String,String> contr(@PathVariable String rrn) throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("value", contService.cont(rrn));
        return map;
    }
}
