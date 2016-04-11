package com.tuibian.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class CBaseObjectMgr {

	public CBaseObject m_Parent = null;
	public Boolean m_bIsLoad = false;
	public String m_sWhere = "";
	public String m_sOrderby = "";

	protected List<CBaseObject> m_lstObj = new ArrayList<CBaseObject>();
	protected Map<UUID, CBaseObject> m_sortObj = new HashMap<UUID, CBaseObject>();
	public CContext Ctx = null;

	public Date m_dtimeLastUpdated = null;

	public CBaseObjectMgr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			m_dtimeLastUpdated = sdf.parse("1900-1-1");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Boolean InsertNew(int iLocation, CBaseObject obj) {
		return InsertNew(iLocation, obj, false);
	}

	public Boolean InsertNew(int iLocation, CBaseObject obj, Boolean bSave) {
		// 如果是删除的元素并且还没保存，又再次添加
		if (m_sortObj.containsKey(obj.Id)) {
			if (obj.m_CmdType == CmdType.Delete) {
				obj.m_CmdType = CmdType.Update;
				return Update(obj, bSave);
			}
			obj.Ctx = this.Ctx;
			obj.m_ObjectMgr = this;
			return true;
		}
		obj.m_CmdType = CmdType.AddNew;
		obj.Ctx = this.Ctx;
		obj.m_ObjectMgr = this;
		m_lstObj.add(iLocation, obj);
		m_sortObj.put(obj.Id, obj);

		if (bSave) {
			if (obj.AddNew()) {
				return true;
			} else {
				return false;
			}
		} else
			return true;
	}

	public Boolean AddNew(CBaseObject obj) {
		return AddNew(obj, false);
	}

	public Boolean AddNew(CBaseObject obj, Boolean bSave) {
		// 如果是删除的元素并且还没保存，又再次添加
		if (m_sortObj.containsKey(obj.Id)) {
			if (obj.m_CmdType == CmdType.Delete) {
				obj.m_CmdType = CmdType.Update;
				return Update(obj, bSave);
			}
			obj.Ctx = this.Ctx;
			obj.m_ObjectMgr = this;
			return true;
		}
		obj.m_CmdType = CmdType.AddNew;
		obj.Ctx = this.Ctx;
		obj.m_ObjectMgr = this;
		m_lstObj.add(obj);
		m_sortObj.put(obj.Id, obj);

		if (bSave) {
			if (obj.AddNew()) {
				return true;
			} else {
				return false;
			}
		} else
			return true;
	}

	public Boolean Update(CBaseObject obj) {
		return Update(obj, false);
	}

	public Boolean Update(CBaseObject obj, Boolean bSave) {
		if (obj.m_CmdType != CmdType.AddNew && obj.m_CmdType != CmdType.Delete)
			obj.m_CmdType = CmdType.Update;
		if (bSave) {
			if (obj.Update()) {
				return true;
			} else {
				return false;
			}
		} else
			return true;
	}

	public Boolean Delete(CBaseObject obj) {
		return Delete(obj, false);
	}

	public Boolean Delete(CBaseObject obj, Boolean bSave) {
		// 删除下级对象
		Collection<CBaseObjectMgr> c = obj.m_sortSubObjectMgr.values();
		Iterator<CBaseObjectMgr> it = c.iterator();
		for (; it.hasNext();) {
			if (!it.next().RemoveAll(bSave))
				return false;
		}
		//
		obj.m_CmdType = CmdType.Delete;
		if (bSave) {
			if (obj.Delete()) {
				m_sortObj.remove(obj.Id);
				m_lstObj.remove(obj);
			} else
				return false;
		}
		return true;
	}

	public Boolean Delete(UUID id) {
		return Delete(id, false);
	}

	public Boolean Delete(UUID id, Boolean bSave) {
		CBaseObject obj = Find(id);
		if (obj == null)
			return false;
		return Delete(obj, bSave);
	}

	public Boolean RemoveAll() {
		return RemoveAll(false);
	}
	public Boolean RemoveAll2() {
		m_lstObj.clear();
		m_sortObj.clear();
		return RemoveAll(false);
	}

	public Boolean RemoveAll(Boolean bSave) {
		for (int i = 0; i < m_lstObj.size(); i++) {
			CBaseObject obj = m_lstObj.get(i);
			if (!Delete(obj))
				return false;
		}

		if (bSave) {
			if (!Save())
				return false;
		}
		return true;
	}

	public Boolean Save() {
		List<CBaseObject> lstDel = new ArrayList<CBaseObject>();
		for (int i = 0; i < m_lstObj.size(); i++) {
			CBaseObject obj = m_lstObj.get(i);

			CmdType cmdType = obj.m_CmdType;
			if (!obj.Save())
				return false;
			if (cmdType == CmdType.Delete)
				lstDel.add(obj);
		}
		for (int i = 0; i < lstDel.size(); i++) {
			CBaseObject obj = m_lstObj.get(i);

			m_sortObj.remove(obj.Id);
			m_lstObj.remove(obj);
		}
		return true;
	}

	public List<CBaseObject> GetList() {
		return GetList(m_sWhere, null, m_sOrderby);
	}

	public List<CBaseObject> GetList(String sWhere) {
		return GetList(sWhere, null, m_sOrderby);
	}

	public List<CBaseObject> GetList(String sWhere, String sOrderby) {
		return GetList(sWhere, null, sOrderby);
	}

	public List<CBaseObject> GetList(String sWhere, List<String> cmdParas) {
		return GetList(sWhere, cmdParas, m_sOrderby);
	}

	public List<CBaseObject> GetList(String sWhere, List<String> cmdParas,
			String sOrderby) {
		if (!Load(sWhere, cmdParas, sOrderby, false))
			return null;
		// 排除删除的元素
		List<CBaseObject> lstRet = new ArrayList<CBaseObject>();
		for (int i = 0; i < m_lstObj.size(); i++) {
			CBaseObject obj = m_lstObj.get(i);

			if (obj.m_CmdType != CmdType.Delete)
				lstRet.add(obj);
		}
		return lstRet;
	}

	public Boolean Load(String sWhere, List<String> cmdParas, String sOrderby,
			Boolean bReload) {
		if (m_bIsLoad && (!bReload) && m_sWhere.equals(sWhere)
				&& m_sOrderby.equals(sOrderby))
			return true;
		m_bIsLoad = true;
		m_sWhere = sWhere;
		m_sOrderby = sOrderby;

		if (!GetListPO(sWhere, cmdParas, sOrderby))
			return false;
		return true;
	}

	public Boolean Load(String sWhere, String sOrderby, Boolean bReload) {
		return Load(sWhere, null, sOrderby, bReload);
	}

	public Boolean Load(String sWhere, String sOrderby) {
		return Load(sWhere, null, sOrderby, false);
	}

	public Boolean Load(String sWhere, Boolean bReload) {
		return Load(sWhere, null, "", bReload);
	}

	public Boolean Load(String sWhere) {
		return Load(sWhere, null, "", false);
	}

	public Boolean Load() {
		return Load("", null, "", false);
	}

	public CBaseObject Find(UUID id) {

		if (m_sortObj.containsKey(id))
			return m_sortObj.get(id);

		return null;
	}

	public CBaseObject GetFirstObj() {
		for (int i = 0; i < m_lstObj.size(); i++) {
			CBaseObject obj = m_lstObj.get(i);

			if (obj.m_CmdType != CmdType.Delete)
				return obj;
		}
		return null;
	}

	// 抽象方法
	// 从本地数据库加载数据
	public abstract Boolean GetListPO(String sWhere, List<String> cmdParas,
			String sOrderby);

	// 从服务器下载最新数据
	public abstract List<CBaseObject> Download(int iOrderBy/* 0顺序；1倒序 */);
	
	//倒序
	public List<CBaseObject> GetListDesc() {
		List<CBaseObject> lstObj=GetList();
		Collections.sort(lstObj, new Comparator<CBaseObject>() {
			@Override
			public int compare(CBaseObject b1, CBaseObject b2) {
				return -1*b1.Updated.compareTo(b2.Updated);
			}
		});
		return lstObj;
	}
}
