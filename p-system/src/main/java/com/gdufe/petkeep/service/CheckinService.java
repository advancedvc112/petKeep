package com.gdufe.petkeep.service;

import com.gdufe.petkeep.dto.CheckinSaveDTO;
import com.gdufe.petkeep.vo.CheckinVO;

import java.util.List;

/**
 * 打卡动态业务接口
 */
public interface CheckinService {

    /**
     * 发布打卡动态
     */
    void save(CheckinSaveDTO dto);

    /**
     * 查询某只动物的打卡时间轴（按时间倒序）
     */
    List<CheckinVO> timeline(Long animalId);

    /**
     * 删除打卡（本人或管理员）
     */
    void delete(Long id);
}
