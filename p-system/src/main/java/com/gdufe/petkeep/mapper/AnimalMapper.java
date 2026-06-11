package com.gdufe.petkeep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdufe.petkeep.entity.Animal;
import com.gdufe.petkeep.vo.AnimalVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 动物档案 Mapper
 */
@Mapper
public interface AnimalMapper extends BaseMapper<Animal> {

    /**
     * 分页查动物档案 + 关联查打卡数量
     */
    @Select("""
        <script>
            SELECT
                a.id,
                a.name,
                a.type,
                a.area,
                a.cover_img,
                a.description,
                a.create_time,
                a.update_time,
                COALESCE(cc.cnt, 0) AS checkin_count
            FROM tb_animal a
            LEFT JOIN (
                SELECT animal_id, COUNT(*) AS cnt
                FROM tb_checkin
                WHERE deleted = 0
                GROUP BY animal_id
            ) cc ON a.id = cc.animal_id
            WHERE a.deleted = 0
            <if test='name != null and name != ""'>
                AND a.name LIKE CONCAT('%', #{name}, '%')
            </if>
            <if test='type != null'>
                AND a.type = #{type}
            </if>
            ORDER BY a.create_time DESC
        </script>
    """)
    IPage<AnimalVO> selectAnimalPage(Page<?> page,
                                     @Param("name") String name,
                                     @Param("type") Integer type);

    /**
     * 根据 ID 查单个动物详情（含打卡数量）
     */
    @Select("""
        SELECT
            a.id,
            a.name,
            a.type,
            a.area,
            a.cover_img,
            a.description,
            a.create_time,
            a.update_time,
            COALESCE(cc.cnt, 0) AS checkin_count
        FROM tb_animal a
        LEFT JOIN (
            SELECT animal_id, COUNT(*) AS cnt
            FROM tb_checkin
            WHERE deleted = 0
            GROUP BY animal_id
        ) cc ON a.id = cc.animal_id
        WHERE a.id = #{id} AND a.deleted = 0
    """)
    AnimalVO selectAnimalById(@Param("id") Long id);
}
