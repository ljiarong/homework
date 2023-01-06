package cn.edu.xmu.oomall.goods.mapper.po.strategy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xincong yao
 * @date 2020-11-18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseCouponDiscount implements Computable{

	public BaseCouponDiscount(BaseCouponLimitation limitation, long value) {
		this.couponLimitation = limitation;
		this.value = value;
		this.className = this.getClass().getName();
	}

	protected long value;

	protected String className;

	protected BaseCouponLimitation couponLimitation;

	@Override
	public List<Item> compute(List<Item> items) {
		if (!couponLimitation.pass(items)) {
			for (Item oi : items) {
				oi.setCouponActivityId(null);
			}
			return items;
		}

		calcAndSetDiscount(items);

		return items;
	}

	public abstract void calcAndSetDiscount(List<Item> items);


}
