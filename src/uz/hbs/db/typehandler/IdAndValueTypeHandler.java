package uz.hbs.db.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import uz.hbs.beans.IdAndValue;

public class IdAndValueTypeHandler extends BaseTypeHandler<IdAndValue> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, IdAndValue parameter, JdbcType jdbcType) throws SQLException {
		ps.setInt(i, parameter.getId());
	}

	@Override
	public IdAndValue getNullableResult(ResultSet rs, String columnName) throws SQLException {
		if (rs.getObject(columnName) != null)
			return new IdAndValue(rs.getInt(columnName), null);
		else
			return new IdAndValue(-1, null);
	}

	@Override
	public IdAndValue getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		if (rs.getObject(columnIndex) != null)
			return new IdAndValue(rs.getInt(columnIndex), null);
		else
			return new IdAndValue(-1, null);
	}

	@Override
	public IdAndValue getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		if (cs.getObject(columnIndex) != null)
			return new IdAndValue(cs.getInt(columnIndex), null);
		else
			return new IdAndValue(-1, null);
	}
}
