//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.goods.mapper;

import cn.edu.xmu.oomall.goods.mapper.po.ActivityPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityPoMapper extends JpaRepository<ActivityPo, Long> {
}
