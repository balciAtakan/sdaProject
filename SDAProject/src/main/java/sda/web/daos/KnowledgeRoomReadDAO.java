package main.java.sda.web.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.java.sda.web.daos.mapper.KnowledgeRoomMessageRowMapper;
import main.java.sda.web.daos.mapper.KnowledgeRoomRowMapper;
import main.java.sda.web.util.UserRole;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import main.java.sda.web.exception.SDAException;
import main.java.sda.web.views.KnowledgeRoomMessageView;
import main.java.sda.web.views.KnowledgeRoomView;
import main.java.sda.web.views.PersonView;

@Repository
public class KnowledgeRoomReadDAO {

	private static Logger log = LogManager.getLogger(KnowledgeRoomReadDAO.class);
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	public ArrayList<KnowledgeRoomView> getKnowledgeRooms() throws SDAException {
		// statt ? wird :uuid verwendet...
		String sql = "SELECT kr.*,kn_role.role_code ,kn_user.person_id, kn_user.username , krm.uuid as krm_uuid, krm.content, krm.owner, krm.modify_date"
				+ "	FROM knowledge_room kr "
				+ " left join knowledge_room_role kn_role on kr.uuid = kn_role.knowledge_room_id"
				+ " left join knowledge_room_user kn_user on kr.uuid = kn_user.knowledge_room_id "
				+ " left join knowledge_room_message krm on kr.uuid = krm.knowledge_room ";

		Map<String, Object> params = new HashMap<>();

		ArrayList<KnowledgeRoomView> res;
		try {

			res = new ArrayList<>(template.query(sql, params, new KnowledgeRoomRowMapper()));
			ArrayList<KnowledgeRoomView> temp = new ArrayList<>();
			if(res.size() > 0)
			{
				for(int i = 0 ; i < res.size() ; i++)
				{
					String id = res.get(i).getUuid();
					KnowledgeRoomView item = temp.stream().filter(a->a.getUuid().equals(id)).findFirst().orElse(null);
					if(item != null)
					{
						if(!res.get(i).getAllowedRoles().isEmpty()) {
							UserRole role = res.get(i).getAllowedRoles().get(0);
							if (item.getAllowedRoles().stream().noneMatch(a -> a.equals(role))) {
								item.getAllowedRoles().add(role);
							}
						}

						if(!res.get(i).getUsers().isEmpty()) {
							PersonView person = res.get(i).getUsers().get(0);
							if (item.getUsers().stream().noneMatch(a -> a.getUuid().equals(person.getUuid()))) {
								item.getUsers().add(person);
							}
						}

						if(!res.get(i).getHistory().isEmpty()) {
							KnowledgeRoomMessageView message = res.get(i).getHistory().get(0);
							if (message != null && item.getHistory().stream().noneMatch(a -> a.getUuid().equals(message.getUuid()))) {
								item.getHistory().add(message);
							}
						}
					}
					else
						temp.add(res.get(i));
				}
			}
			res = temp;
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			throw new SDAException(e.getMessage());
		}
		
		return res;
	}

	public ArrayList<KnowledgeRoomMessageView> getKnowledgeRoomData(String roomId) throws SDAException {
		String sql = "SELECT * FROM knowledge_room_message krm, person p where knowledge_room = :roomId and krm.owner=p.id order by krm.modify_date";

		Map<String, Object> params = new HashMap<>();

		params.put("roomId", roomId);
		ArrayList<KnowledgeRoomMessageView> res = null;
		try {
			res = (ArrayList<KnowledgeRoomMessageView>) template.query(sql, params,
					new KnowledgeRoomMessageRowMapper());

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			throw new SDAException(e.getMessage());
		}
		
		return res;
	}
	
	public boolean deleteKnowledgeRoom(String roomname)throws SDAException{
		
		String sql = "DELETE FROM knowledge_room WHERE roomname = :roomname";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("roomname", roomname);
		
		try {
			template.update(sql, parameters);
			log.info("Room is deleted!");
		}
		catch (Exception e) {
			if(e instanceof DataIntegrityViolationException)
			{
				log.info("Roomowner!!");
				throw new SDAException("This room cannot be deleted!! ");
			}
			else
			{
				e.printStackTrace();
				log.info("Error in DB!");
				throw new SDAException("Error in DB in deleteRoom process!");
			}
		}
		return true;
	}
	
}
