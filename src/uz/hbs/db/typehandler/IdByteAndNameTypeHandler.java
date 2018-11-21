package uz.hbs.db.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import uz.hbs.beans.IdByteAndName;

public class IdByteAndNameTypeHandler extends BaseTypeHandler<IdByteAndName> {
	public static final byte UNKNOWN = -1;

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, IdByteAndName parameter, JdbcType jdbcType) throws SQLException {
		ps.setInt(i, parameter.getId());
	}

	@Override
	public IdByteAndName getNullableResult(ResultSet rs, String columnName) throws SQLException {
		if (rs.getObject(columnName) != null)
			return new IdByteAndName(rs.getByte(columnName), null);
		else
			return new IdByteAndName(UNKNOWN, null);
	}

	@Override
	public IdByteAndName getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		if (rs.getObject(columnIndex) != null)
			return new IdByteAndName(rs.getByte(columnIndex), null);
		else
			return new IdByteAndName(UNKNOWN, null);
	}

	@Override
	public IdByteAndName getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		if (cs.getObject(columnIndex) != null)
			return new IdByteAndName(cs.getByte(columnIndex), null);
		else
			return new IdByteAndName(UNKNOWN, null);
	}
}
