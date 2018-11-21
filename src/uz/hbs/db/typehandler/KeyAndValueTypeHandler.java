package uz.hbs.db.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import uz.hbs.beans.KeyAndValue;

public class KeyAndValueTypeHandler extends BaseTypeHandler<KeyAndValue> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, KeyAndValue parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.getKey());
	}

	@Override
	public KeyAndValue getNullableResult(ResultSet rs, String columnName) throws SQLException {
		if (rs.getObject(columnName) != null)
			return new KeyAndValue(rs.getString(columnName), null);
		else
			return new KeyAndValue("", null);
	}

	@Override
	public KeyAndValue getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		if (rs.getObject(columnIndex) != null)
			return new KeyAndValue(rs.getString(columnIndex), null);
		else
			return new KeyAndValue("", null);
	}

	@Override
	public KeyAndValue getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		if (cs.getObject(columnIndex) != null)
			return new KeyAndValue(cs.getString(columnIndex), null);
		else
			return new KeyAndValue("", null);
	}
}
