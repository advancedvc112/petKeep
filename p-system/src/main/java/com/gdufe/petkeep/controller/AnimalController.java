package com.gdufe.petkeep.controller;

import com.gdufe.petkeep.common.BusinessException;
import com.gdufe.petkeep.common.R;
import com.gdufe.petkeep.common.UserContext;
import com.gdufe.petkeep.dto.AnimalQueryDTO;
import com.gdufe.petkeep.dto.AnimalSaveDTO;
import com.gdufe.petkeep.service.AnimalService;
import com.gdufe.petkeep.vo.AnimalPageVO;
import com.gdufe.petkeep.vo.AnimalVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 动物档案接口
 *
 * 普通用户：GET 查看
 * 管理员：GET/POST/PUT/DELETE 全部
 */
@RestController
@RequestMapping("/api/animal")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    /**
     * GET /api/animal/page?pageNum=1&pageSize=10&name=小&type=0
     * <p>
     * 分页查询 + 模糊搜索 + 类型筛选
     */
    @GetMapping("/page")
    public R<AnimalPageVO> page(AnimalQueryDTO dto) {
        return R.ok(animalService.page(dto));
    }

    /**
     * GET /api/animal/{id}
     * <p>
     * 查询动物详情（含打卡数量）
     */
    @GetMapping("/{id}")
    public R<AnimalVO> getById(@PathVariable Long id) {
        return R.ok(animalService.getById(id));
    }

    /**
     * POST /api/animal
     * <p>
     * 新增动物档案（仅管理员）
     * 请求体：{ "name":"小橘", "type":0, "area":"图书馆草坪", "coverImg":"/animal/xxx.jpg", "description":"..." }
     */
    @PostMapping
    public R<Void> save(@Valid @RequestBody AnimalSaveDTO dto) {
        checkAdmin();
        animalService.save(dto);
        return R.ok();
    }

    /**
     * PUT /api/animal
     * <p>
     * 更新动物档案（仅管理员）
     */
    @PutMapping
    public R<Void> update(@Valid @RequestBody AnimalSaveDTO dto) {
        checkAdmin();
        animalService.update(dto);
        return R.ok();
    }

    /**
     * DELETE /api/animal/{id}
     * <p>
     * 删除动物档案（仅管理员，逻辑删除）
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        checkAdmin();
        animalService.delete(id);
        return R.ok();
    }

    /** 校验当前用户是否为管理员 */
    private void checkAdmin() {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "无权限，仅管理员可操作");
        }
    }
}
