package uz.hbs.db.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import uz.hbs.enums.ActionRight;

public class ActionRightTypeHandler extends BaseTypeHandler<ActionRight> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, ActionRight parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.name());
	}

	@Override
	public ActionRight getNullableResult(ResultSet rs, String columnName) throws SQLException {
		if(rs.getObject(columnName) != null) {
			return ActionRight.getByName(rs.getString(columnName));
		} 
		return ActionRight.UNKNOWN;
	}

	@Override
	public ActionRight getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		if(rs.getObject(columnIndex) != null) {
			return ActionRight.getByName(rs.getString(columnIndex));
		} 
		return ActionRight.UNKNOWN;
	}

	@Override
	public ActionRight getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		if(cs.getObject(columnIndex) != null) {
			return ActionRight.getByName(cs.getString(columnIndex));
		} 
		return ActionRight.UNKNOWN;
	}
}
