package com.gdufe.petkeep.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdufe.petkeep.common.BusinessException;
import com.gdufe.petkeep.config.MinioConfig;
import com.gdufe.petkeep.dto.AnimalQueryDTO;
import com.gdufe.petkeep.dto.AnimalSaveDTO;
import com.gdufe.petkeep.entity.Animal;
import com.gdufe.petkeep.mapper.AnimalMapper;
import com.gdufe.petkeep.service.AnimalService;
import com.gdufe.petkeep.utils.MinioUtils;
import com.gdufe.petkeep.vo.AnimalPageVO;
import com.gdufe.petkeep.vo.AnimalVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 动物档案业务实现
 */
@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalMapper animalMapper;
    private final MinioUtils minioUtils;

    @Override
    public AnimalPageVO page(AnimalQueryDTO dto) {
        Page<AnimalVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        IPage<AnimalVO> result = animalMapper.selectAnimalPage(page, dto.getName(), dto.getType());

        AnimalPageVO pageVO = new AnimalPageVO();
        pageVO.setTotal(result.getTotal());
        pageVO.setPageNum(result.getCurrent());
        pageVO.setPageSize(result.getSize());
        pageVO.setRecords(result.getRecords());
        return pageVO;
    }

    @Override
    public AnimalVO getById(Long id) {
        AnimalVO vo = animalMapper.selectAnimalById(id);
        if (vo == null) {
            throw new BusinessException(404, "动物档案不存在");
        }
        return vo;
    }

    @Override
    public void save(AnimalSaveDTO dto) {
        Animal animal = new Animal();
        animal.setName(dto.getName());
        animal.setType(dto.getType());
        animal.setArea(dto.getArea());
        animal.setCoverImg(dto.getCoverImg());
        animal.setDescription(dto.getDescription());
        animal.setCreateTime((LocalDateTime.now()));
        animal.setUpdateTime((LocalDateTime.now()));
        animalMapper.insert(animal);
    }

    @Override
    public void update(AnimalSaveDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("更新操作必须提供 ID");
        }
        Animal animal = animalMapper.selectById(dto.getId());
        if (animal == null) {
            throw new BusinessException(404, "动物档案不存在");
        }
        animal.setName(dto.getName());
        animal.setType(dto.getType());
        animal.setArea(dto.getArea());
        animal.setCoverImg(dto.getCoverImg());
        animal.setDescription(dto.getDescription());
        animal.setUpdateTime(LocalDateTime.now());
        animalMapper.updateById(animal);
    }

    @Override
    public void delete(Long id) {
        if (animalMapper.selectById(id) == null) {
            throw new BusinessException(404, "动物档案不存在");
        }
        animalMapper.deleteById(id);  // MyBatis-Plus 逻辑删除
    }
}
