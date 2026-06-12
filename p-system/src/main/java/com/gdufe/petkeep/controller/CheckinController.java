package com.gdufe.petkeep.controller;

import com.gdufe.petkeep.common.R;
import com.gdufe.petkeep.dto.CheckinSaveDTO;
import com.gdufe.petkeep.service.CheckinService;
import com.gdufe.petkeep.vo.CheckinVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 打卡动态接口
 *
 * 所有接口均需登录（Token）
 */
@RestController
@RequestMapping("/api/checkin")
@RequiredArgsConstructor
public class CheckinController {

    private final CheckinService checkinService;

    /**
     * POST /api/checkin
     * <p>
     * 发布打卡动态
     * 请求体：{ "animalId":1, "content":"今天看到它了...", "imgUrl":"/checkin/xxx.jpg" }
     */
    @PostMapping
    public R<Void> save(@Valid @RequestBody CheckinSaveDTO dto) {
        checkinService.save(dto);
        return R.ok();
    }

    /**
     * GET /api/checkin/animal/{animalId}
     * <p>
     * 获取某只动物的打卡时间轴（按时间倒序，最新在前）
     * 返回：List<CheckinVO>（含动物名、打卡用户昵称、内容、图片、时间）
     */
    @GetMapping("/animal/{animalId}")
    public R<List<CheckinVO>> timeline(@PathVariable Long animalId) {
        return R.ok(checkinService.timeline(animalId));
    }

    /**
     * DELETE /api/checkin/{id}
     * <p>
     * 删除打卡动态（本人可删自己的，管理员可删任意）
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        checkinService.delete(id);
        return R.ok();
    }
}
