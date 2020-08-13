package com.csbr.cloud.canal.client.abstracts.option.content;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.csbr.cloud.canal.client.abstracts.option.AbstractDBOption;

/**
 * 更新数据
 */
public abstract class UpdateOption extends AbstractDBOption {
	
	
	/**
	 * 设置更新属性
	 *
	 * @param
	 * @return
	 * @author 阿导
	 * @time 2018/5/29 09:27
	 * @CopyRight 万物皆导
	 */
	@Override
	protected void setEventType() {
		this.eventType = CanalEntry.EventType.UPDATE;
	}
	
}
