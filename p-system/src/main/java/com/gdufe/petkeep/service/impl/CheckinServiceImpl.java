package com.gdufe.petkeep.service.impl;

import com.gdufe.petkeep.common.BusinessException;
import com.gdufe.petkeep.common.UserContext;
import com.gdufe.petkeep.dto.CheckinSaveDTO;
import com.gdufe.petkeep.entity.Checkin;
import com.gdufe.petkeep.mapper.AnimalMapper;
import com.gdufe.petkeep.mapper.CheckinMapper;
import com.gdufe.petkeep.service.CheckinService;
import com.gdufe.petkeep.utils.MinioUtils;
import com.gdufe.petkeep.vo.CheckinVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 打卡动态业务实现
 */
@Service
@RequiredArgsConstructor
public class CheckinServiceImpl implements CheckinService {

    private final CheckinMapper checkinMapper;
    private final AnimalMapper animalMapper;
    private final MinioUtils minioUtils;

    @Override
    public void save(CheckinSaveDTO dto) {
        // 校验动物存在
        if (animalMapper.selectById(dto.getAnimalId()) == null) {
            throw new BusinessException(404, "动物档案不存在");
        }
        Checkin checkin = new Checkin();
        checkin.setAnimalId(dto.getAnimalId());
        checkin.setUserId(UserContext.getUserId());
        checkin.setContent(dto.getContent());
        checkin.setImgUrl(dto.getImgUrl());
        checkinMapper.insert(checkin);
    }

    @Override
    public List<CheckinVO> timeline(Long animalId) {
        List<CheckinVO> list = checkinMapper.selectTimelineByAnimalId(animalId);
        // 补全图片完整 URL
        for (CheckinVO vo : list) {
            vo.setImgUrl(minioUtils.getAccessUrl(vo.getImgUrl()));
        }
        return list;
    }

    @Override
    public void delete(Long id) {
        Checkin checkin = checkinMapper.selectById(id);
        if (checkin == null) {
            throw new BusinessException(404, "打卡记录不存在");
        }
        // 权限检查：只能删自己的打卡，管理员可删任意
        if (!UserContext.isAdmin() && !checkin.getUserId().equals(UserContext.getUserId())) {
            throw new BusinessException(403, "无权删除他人打卡");
        }
        checkinMapper.deleteById(id);
    }
}
