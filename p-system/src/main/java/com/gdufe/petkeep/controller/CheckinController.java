package com.gdufe.petkeep.controller;

import com.gdufe.petkeep.common.R;
import com.gdufe.petkeep.dto.CheckinSaveDTO;
import com.gdufe.petkeep.service.CheckinService;
import com.gdufe.petkeep.vo.CheckinVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 打卡动态接口
 *
 * 所有接口均需登录（Token）
 */
@Tag(name = "打卡动态", description = "动物打卡动态的发布、查询、删除")
@RestController
@RequestMapping("/api/checkin")
@RequiredArgsConstructor
public class CheckinController {

    private final CheckinService checkinService;

    /**
     * GET /api/checkin/list/{animalId}
     * 获取某只动物的打卡时间轴（按时间倒序，最新在前）
     * 返回：List<CheckinVO>（含动物名、打卡用户昵称、内容、图片、时间）
     */
    @Operation(summary = "获取打卡列表", description = "获取指定动物的打卡时间轴，按时间倒序排列")
    @GetMapping("/list/{animalId}")
    public R<List<CheckinVO>> list(@Parameter(description = "动物ID") @PathVariable Long animalId) {
        return R.ok(checkinService.timeline(animalId));
    }

    /**
     * POST /api/checkin/add
     * 发布打卡动态
     * 请求体：{ "animalId":1, "content":"今天看到它了...", "imgUrl":"/checkin/xxx.jpg" }
     */
    @Operation(summary = "发布打卡", description = "发布新的打卡动态")
    @PostMapping("/add")
    public R<Void> add(@Valid @RequestBody CheckinSaveDTO dto) {
        checkinService.save(dto);
        return R.ok();
    }

    /**
     * POST /api/checkin/remove/{id}
     * 删除打卡动态（本人可删自己的，管理员可删任意）
     */
    @Operation(summary = "删除打卡", description = "删除打卡动态（本人可删，管理员可删任意）")
    @PostMapping("/remove/{id}")
    public R<Void> remove(@Parameter(description = "打卡ID") @PathVariable Long id) {
        checkinService.delete(id);
        return R.ok();
    }
}
