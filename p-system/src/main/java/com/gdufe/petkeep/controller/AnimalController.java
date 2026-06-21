package com.gdufe.petkeep.controller;

import com.gdufe.petkeep.common.BusinessException;
import com.gdufe.petkeep.common.R;
import com.gdufe.petkeep.common.UserContext;
import com.gdufe.petkeep.dto.AnimalQueryDTO;
import com.gdufe.petkeep.dto.AnimalSaveDTO;
import com.gdufe.petkeep.service.AnimalService;
import com.gdufe.petkeep.vo.AnimalPageVO;
import com.gdufe.petkeep.vo.AnimalVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 动物档案接口
 *
 * 普通用户：查询
 * 管理员：查询、新增、更新、删除
 */
@Tag(name = "动物档案", description = "动物档案的增删改查")
@RestController
@RequestMapping("/api/animal")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    /**
     * GET /api/animal/list
     * 分页查询 + 模糊搜索 + 类型筛选
     */
    @Operation(summary = "分页查询动物列表", description = "支持按名称模糊搜索、按类型筛选")
    @GetMapping("/list")
    public R<AnimalPageVO> list(AnimalQueryDTO dto) {
        return R.ok(animalService.page(dto));
    }

    /**
     * GET /api/animal/detail/{id}
     * 查询动物详情（含打卡数量）
     */
    @Operation(summary = "查询动物详情", description = "根据ID查询动物详情，含打卡数量")
    @GetMapping("/detail/{id}")
    public R<AnimalVO> detail(@Parameter(description = "动物ID") @PathVariable Long id) {
        return R.ok(animalService.getById(id));
    }

    /**
     * POST /api/animal/add
     * 新增动物档案（仅管理员）
     */
    @Operation(summary = "新增动物档案", description = "创建新的动物档案（仅管理员可操作）")
    @PostMapping("/add")
    public R<Void> add(@Valid @RequestBody AnimalSaveDTO dto) {
        checkAdmin();
        animalService.save(dto);
        return R.ok();
    }

    /**
     * POST /api/animal/update
     * 更新动物档案（仅管理员）
     */
    @Operation(summary = "更新动物档案", description = "更新动物档案信息（仅管理员可操作）")
    @PostMapping("/update")
    public R<Void> update(@Valid @RequestBody AnimalSaveDTO dto) {
        checkAdmin();
        animalService.update(dto);
        return R.ok();
    }

    /**
     * POST /api/animal/remove/{id}
     * 删除动物档案（仅管理员，逻辑删除）
     */
    @Operation(summary = "删除动物档案", description = "删除动物档案，支持逻辑删除（仅管理员可操作）")
    @PostMapping("/remove/{id}")
    public R<Void> remove(@Parameter(description = "动物ID") @PathVariable Long id) {
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
