package io.github.xddcode.wand.core.rest;

import io.github.xddcode.wand.core.dynamic.DynamicClass;
import io.github.xddcode.wand.core.dynamic.DynamicController;
import io.github.xddcode.wand.core.dynamic.SourceType;
import io.github.xddcode.wand.core.expose.ExposeContext;
import io.github.xddcode.wand.core.expose.ExposeResource;
import io.github.xddcode.wand.core.utils.WandResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wand")
public class DefaultRest {

    @Autowired
    private ExposeContext exposeContext;

    @PostMapping("inject")
    public WandResponse inject(@RequestParam("javaFilePath") String javaFilePath, @RequestParam("fullClassName") String fullClassName) {
        //从路径上获取.java文件内容
        DynamicClass dynamicClass = DynamicClass.init(SourceType.AliOSS, javaFilePath, fullClassName);
        DynamicController.builder()
                .dynamicClass(dynamicClass)
                .build()
                .load();
        return WandResponse.success();
    }

    @DeleteMapping("eject")
    public WandResponse eject(@RequestParam("fullClassName") String fullClassName) {
        DynamicClass dynamicClass = DynamicClass.init("", fullClassName);
        DynamicController.builder()
                .dynamicClass(dynamicClass)
                .build()
                .unload();
        return WandResponse.success();
    }

    @GetMapping("context-exposed")
    public WandResponse getExposeMethods() {
        ExposeResource data = exposeContext.init();
        return WandResponse.success(data);
    }
}
