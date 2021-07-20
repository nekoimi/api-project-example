package com.nekoimi.boot.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nekoimi.boot.framework.mybatis.entity.BaseEntity;
import com.nekoimi.boot.framework.http.JsonResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nekoimi.boot.modules.user.data.LoginResult;
import com.nekoimi.boot.modules.user.entity.User;
import com.nekoimi.boot.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nekoimi.boot.framework.http.BaseController;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * User Controller
 *
 * nekoimi  2021-07-20
 */
@RestController
public class UserController extends BaseController {

    @Autowired
    private UserService targetService;

    @PostMapping("api/v1/test_login")
    public JsonResponse login(@RequestBody Map<String, Object> map) {
        LoginResult result = targetService.login(map.getOrDefault("mobile", "").toString(), "");
        return ok(result);
    }

    /**
     * 获取分页列表
     *
     * @return
     */
    @GetMapping("api/v1/user/list")
    public JsonResponse list() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BaseEntity::getCreatedAt);
        IPage<User> paginator = targetService.listPaginator(page(), pageSize(), wrapper);
        return ok(paginator);
    }

    /**
    * 获取下拉列表
    *
    * @return
    */
    @GetMapping("api/v1/user/down")
    public JsonResponse down() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BaseEntity::getCreatedAt);
        List<User> list = targetService.findAll(wrapper);
        return ok("list", list);
    }

    /**
     * 根据id获取数据
     *
     * @param id
     * @return
     */
    @GetMapping("api/v1/user/{id}")
    public JsonResponse get(@PathVariable("id") Serializable id) {
        User result = targetService.getByOrFail(id);
        return ok(result);
    }

    /**
     * 添加数据
     *
     * @return
     */
    @PostMapping("api/v1/user")
    public JsonResponse create(@RequestBody Map<String, Object> map) {
        Serializable id = targetService.create(map);
        User result = targetService.getBy(id);
        return ok(result);
    }

    /**
     * 根据id更新数据
     *
     * @param map
     * @return
     */
    @PutMapping("api/v1/user/{id}")
    public JsonResponse update(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        targetService.update(id, map);
        return ok();
    }

    /**
     * 根据id删除数据
     *
     * @param id
     * @return
     */
    @DeleteMapping("api/v1/user/{id}")
    public JsonResponse remove(@PathVariable("id") String id) {
        targetService.removeBy(id);
        return ok();
    }

    /**
     * 批量删除数据
     *
     * @return
     */
    @DeleteMapping("api/v1/user/batch")
    public JsonResponse removeBatch(@RequestParam(value = "ids", required = false) String ids) {
        if (ids == null || ids.trim().length() <= 0) {
            return ok(); // Ignore.
        }
        List<String> list = Arrays.asList(ids.trim().split("[,]"));
        targetService.removeBy(list);
        return ok();
    }

}
