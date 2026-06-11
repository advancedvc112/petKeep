package com.gdufe.petkeep.service;

import com.gdufe.petkeep.dto.AnimalQueryDTO;
import com.gdufe.petkeep.dto.AnimalSaveDTO;
import com.gdufe.petkeep.vo.AnimalVO;
import com.gdufe.petkeep.vo.AnimalPageVO;

/**
 * 动物档案业务接口
 */
public interface AnimalService {

    /**
     * 分页查询动物档案（+ 打卡计数）
     */
    AnimalPageVO page(AnimalQueryDTO dto);

    /**
     * 查询动物详情
     */
    AnimalVO getById(Long id);

    /**
     * 新增动物档案（管理员）
     */
    void save(AnimalSaveDTO dto);

    /**
     * 更新动物档案（管理员）
     */
    void update(AnimalSaveDTO dto);

    /**
     * 删除动物档案（管理员，逻辑删除）
     */
    void delete(Long id);
}
