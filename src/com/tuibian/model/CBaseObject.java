package com.tuibian.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.tuibian.util.VersionUtil;

enum CmdType {
	None, AddNew, Update, Delete
}

public abstract class CBaseObject {

	public CBaseObjectMgr m_ObjectMgr = null;

	public Object m_objTempData = null; // 用来保存临时数据

	public CmdType m_CmdType = CmdType.None;

	// 下级管理器对象
	public Map<String, CBaseObjectMgr> m_sortSubObjectMgr = new HashMap<String, CBaseObjectMgr>();

	public CContext Ctx = null;

	public CBaseObject() {
		Id = UUID.randomUUID();

		Created = new Date(System.currentTimeMillis());
		Creator = VersionUtil.getEmptyUUID();
		Updated = new Date(System.currentTimeMillis());
		Updator = VersionUtil.getEmptyUUID();

	}

	// 属性
	public UUID Id;

	public Date Created;

	public UUID Creator;

	public Date Updated;

	public UUID Updator;

	public final UUID getId() {
		return Id;
	}

	public final void setId(UUID value) {
		Id = value;
	}

	// 对象持久化
	public Boolean AddNew() {
		if (m_CmdType != CmdType.AddNew)
			return false;
		if (AddNewPO())
			m_CmdType = CmdType.None;
		else
			return false;
		return true;
	}

	public Boolean Update() {
		if (m_CmdType != CmdType.Update)
			return false;
		if (UpdatePO())
			m_CmdType = CmdType.None;
		else
			return false;
		return true;
	}

	public Boolean Delete() {
		if (m_CmdType != CmdType.Delete)
			return false;
		return DeletePO();
	}

	public Boolean Save() {
		if (m_CmdType == CmdType.AddNew) {
			if (!AddNew())
				return false;
		} else if (m_CmdType == CmdType.Update) {
			if (!Update())
				return false;
		} else if (m_CmdType == CmdType.Delete) {
			if (!Delete())
				return false;
		}

		// 保存下级对象
		Collection<CBaseObjectMgr> c = m_sortSubObjectMgr.values();
		Iterator<CBaseObjectMgr> it = c.iterator();
		for (; it.hasNext();) {
			if (!it.next().Save())
				return false;
		}

		return true;
	}

	// 抽象方法
	public abstract Boolean AddNewPO();

	public abstract Boolean UpdatePO();

	public abstract Boolean DeletePO();

}
