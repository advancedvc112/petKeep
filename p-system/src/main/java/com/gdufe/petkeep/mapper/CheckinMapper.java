package com.gdufe.petkeep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdufe.petkeep.entity.Checkin;
import com.gdufe.petkeep.vo.CheckinVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 打卡动态 Mapper
 */
@Mapper
public interface CheckinMapper extends BaseMapper<Checkin> {

    /**
     * 根据动物 ID 查时间轴（关联动物名 + 用户昵称，按时间倒序）
     */
    @Select("""
        SELECT
            c.id,
            c.animal_id,
            a.name AS animal_name,
            u.nickname AS user_nickname,
            c.content,
            c.img_url,
            c.create_time
        FROM tb_checkin c
        JOIN tb_animal a ON c.animal_id = a.id
        JOIN tb_user u ON c.user_id = u.id
        WHERE c.animal_id = #{animalId}
          AND c.deleted = 0
        ORDER BY c.create_time DESC
    """)
    List<CheckinVO> selectTimelineByAnimalId(@Param("animalId") Long animalId);
}
