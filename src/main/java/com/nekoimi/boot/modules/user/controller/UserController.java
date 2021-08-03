package com.nekoimi.boot.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nekoimi.boot.common.annotaction.LoginRequired;
import com.nekoimi.boot.common.annotaction.Operator;
import com.nekoimi.boot.framework.http.BaseController;
import com.nekoimi.boot.framework.http.PaginatorResult;
import com.nekoimi.boot.framework.mybatis.entity.BaseEntity;
import com.nekoimi.boot.modules.user.data.LoginResult;
import com.nekoimi.boot.modules.user.entity.User;
import com.nekoimi.boot.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("ajax_test")
    public String ajaxTest() {
        return "hello world";
    }

    @PostMapping("api/v1/test_login")
    public LoginResult login(@RequestBody Map<String, Object> map) {
        LoginResult result = targetService.login(map.getOrDefault("mobile", "").toString(), "");
        return result;
    }

    @LoginRequired
    @GetMapping("api/v1/user/me")
    public User me(@Operator User user) {
        return user;
    }

    /**
     * 获取分页列表
     *
     * @return
     */
    @LoginRequired
    @GetMapping("api/v1/user/list")
    public PaginatorResult<User> list() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BaseEntity::getCreatedAt);
        IPage<User> paginator = targetService.listPaginator(page(), pageSize(), wrapper);
        return new PaginatorResult<>(paginator);
    }

    /**
    * 获取下拉列表
    *
    * @return
    */
    @LoginRequired
    @GetMapping("api/v1/user/down")
    public Map down() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BaseEntity::getCreatedAt);
        List<User> list = targetService.findAll(wrapper);
        return Map.of("list", list);
    }

    /**
     * 根据id获取数据
     *
     * @param id
     * @return
     */
    @LoginRequired
    @GetMapping("api/v1/user/{id}")
    public User get(@PathVariable("id") Serializable id) {
        User result = targetService.getByOrFail(id);
        return result;
    }

    /**
     * 添加数据
     *
     * @return
     */
    @LoginRequired
    @PostMapping("api/v1/user")
    public User create(@RequestBody Map<String, Object> map) {
        Serializable id = targetService.create(map);
        User result = targetService.getBy(id);
        return result;
    }

    /**
     * 根据id更新数据
     *
     * @param map
     * @return
     */
    @LoginRequired
    @PutMapping("api/v1/user/{id}")
    public void update(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        targetService.update(id, map);
    }

    /**
     * 根据id删除数据
     *
     * @param id
     * @return
     */
    @LoginRequired
    @DeleteMapping("api/v1/user/{id}")
    public void remove(@PathVariable("id") String id) {
        targetService.removeBy(id);
    }

    /**
     * 批量删除数据
     *
     * @return
     */
    @LoginRequired
    @DeleteMapping("api/v1/user/batch")
    public void removeBatch(@RequestParam(value = "ids", required = false) String ids) {
        if (ids == null || ids.trim().length() <= 0) {
            return; // Ignore.
        }
        List<String> list = Arrays.asList(ids.trim().split("[,]"));
        targetService.removeBy(list);
    }

}
