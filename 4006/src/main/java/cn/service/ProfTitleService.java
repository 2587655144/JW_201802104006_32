package cn.service;


import cn.dao.ProfTitleDao;
import cn.domain.ProfTitle;

import java.sql.SQLException;
import java.util.Collection;

public final class ProfTitleService {
	private static ProfTitleDao profTitleDao = ProfTitleDao.getInstance();
	private static ProfTitleService profTitleService = new ProfTitleService();

	private ProfTitleService() {
	}

	public static ProfTitleService getInstance() {
		return profTitleService;
	}

	public Collection<ProfTitle> getAll() throws SQLException {
		return profTitleDao.findAll();
	}

	public ProfTitle find(Integer id) throws SQLException {
		return profTitleDao.find(id);
	}

	public boolean update(ProfTitle profTitle) throws SQLException {
		return profTitleDao.update(profTitle);
	}

	public boolean add(ProfTitle profTitle) throws SQLException {
		return profTitleDao.add(profTitle);
	}

	public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
		return ProfTitleDao.delete(id);
	}
}

