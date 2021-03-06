package main.java.sda.web.daos.mapper;

import main.java.sda.web.util.SDAUtil;
import main.java.sda.web.util.UserRole;
import main.java.sda.web.views.PersonView;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonRowMapper implements RowMapper<PersonView>
{

    boolean needRoles;

    public PersonRowMapper(boolean needRoles)
    {
        this.needRoles = needRoles;
    }

    @Override
    public PersonView mapRow(ResultSet resultSet, int arg1) throws SQLException
    {
        PersonView personView = new PersonView();
        personView.setUuid(resultSet.getString("id"));
        personView.setFirstname(SDAUtil.firstLetterUppercase(resultSet.getString("firstname")));
        personView.setLastname(SDAUtil.firstLetterUppercase(resultSet.getString("lastname")));
        personView.setModify_date(resultSet.getDate("modify_date"));
        personView.setUsername(resultSet.getString("username"));

        if (needRoles)
        {
            ArrayList<UserRole> list = new ArrayList<>();
            list.add(UserRole.getEnum(resultSet.getString("role_code").toLowerCase()));
            personView.setRoles(list);
        }

        return personView;

    }


}
