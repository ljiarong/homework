//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.shop.mapper;

import cn.edu.xmu.oomall.shop.mapper.po.ShopServiceProductPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShopServiceProductPoMapper extends JpaRepository<ShopServiceProductPo, Long> {
        Page<ShopServiceProductPo> findByMaintainerId(Long maintainerId, Pageable pageable);
        Page<ShopServiceProductPo> findByMaintainerIdEqualsAndInvalidEqualsAndBeginTimeBeforeAndEndTimeAfter(Long maintainerId, Byte invalid, LocalDateTime beginTime, LocalDateTime endTime, Pageable pageable);
        Page<ShopServiceProductPo> findByProductIdAndRegionIdOrderByPriority(Long productId, Long regionId, Pageable pageable);
}
