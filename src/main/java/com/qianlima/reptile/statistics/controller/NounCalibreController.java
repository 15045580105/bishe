package com.qianlima.reptile.statistics.controller;

import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.service.NounCalibreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: NounCalibreController
 * @date 2020/3/20 11:27 上午
 */
@RestController
@RequestMapping("/nounCalibre")
public class NounCalibreController {
    @Autowired
    private NounCalibreService nounCalibreService;

    @PostMapping("/add")
    public Response addNounCalibre(String operator, String content) {
        return nounCalibreService.addNounCalibre(operator, content);
    }

    @PutMapping("/update")
    public Response updateNounCalibre(String id, String operator, String content) {
        return nounCalibreService.updateNounCalibre(id, operator, content);
    }

    @DeleteMapping("/delete")
    public Response deleteNounCalibre(String id) {
        return nounCalibreService.deleteNounCalibre(id);
    }

    @GetMapping("/query")
    public Response queryNounCalibre() {
        return nounCalibreService.queryNounCalibre();
    }
}
