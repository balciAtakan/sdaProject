package main.java.sda.web.daos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.sda.web.daos.mapper.DfxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import main.java.sda.web.views.DfxView;

@Repository
public class DfxDAO {

	@Autowired
	private NamedParameterJdbcTemplate template;


	public List<DfxView> findAll() {
		
		String sql = "SELECT * FROM categories";

		Map<String, Object> parameters = 
				new HashMap<>();

		return (List<DfxView>)template.query(sql, parameters, 
				new DfxMapper());
	}
}
