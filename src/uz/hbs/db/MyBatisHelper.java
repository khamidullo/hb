package uz.hbs.db;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyBatisHelper {
	private static Logger logger = LoggerFactory.getLogger(MyBatisHelper.class);
	private static SqlSessionFactory sqlSessionFactory;

	static {
		try {
			// String systemTimeZone = MyWebApplication.getConfigBundle().getString("system.timezone");
			// TimeZone timeZone = TimeZone.getTimeZone(systemTimeZone);
			// TimeZone.setDefault(timeZone);
			InputStream inputStream = Resources.getResourceAsStream("uz/hbs/db/MyBatisConfig.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (Throwable e) {
			logger.error("Exception during SQL map building: ", e);
		}
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	/**
	 * Retrieve a single row mapped from the statement key
	 * 
	 * @param <T>
	 *            the returned object type
	 * @param statement
	 * @return Mapped object
	 */
	public <T> T selectOne(String statement) {
		return selectOne(statement, null);
	}

	/**
	 * Retrieve a single row mapped from the statement key and parameter.
	 * 
	 * @param <T>
	 *            the returned object type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @return Mapped object
	 */
	public <T> T selectOne(String statement, Object parameter) {
		SqlSession sqlSession = sqlSessionFactory.openSession(false);
		T result = null;
		try {
			result = sqlSession.selectOne(statement, parameter);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * Retrieve a list of mapped objects from the statement key and parameter.
	 * 
	 * @param <E>
	 *            the returned list element type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @return List of mapped object
	 */
	public <E> List<E> selectList(String statement) {
		return selectList(statement, null);
	}

	/**
	 * Retrieve a list of mapped objects from the statement key and parameter.
	 * 
	 * @param <E>
	 *            the returned list element type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @return List of mapped object
	 */
	public <E> List<E> selectList(String statement, Object parameter) {
		return selectList(statement, parameter, RowBounds.DEFAULT);
	}

	/**
	 * Retrieve a list of mapped objects from the statement key and parameter,
	 * within the specified row bounds.
	 * 
	 * @param <E>
	 *            the returned list element type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @param rowBounds
	 *            Bounds to limit object retrieval
	 * @return List of mapped object
	 */
	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		SqlSession sqlSession = sqlSessionFactory.openSession(false);
		List<E> result = Collections.emptyList();
		try {
			result = sqlSession.selectList(statement, parameter, rowBounds);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * The selectMap is a special case in that it is designed to convert a list
	 * of results into a Map based on one of the properties in the resulting
	 * objects.
	 * Eg. Return a of Map[Integer,Author] for selectMap("selectAuthors","id")
	 * 
	 * @param <K>
	 *            the returned Map keys type
	 * @param <V>
	 *            the returned Map values type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param mapKey
	 *            The property to use as key for each value in the list.
	 * @return Map containing key pair data.
	 */
	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
		return selectMap(statement, null, mapKey);
	}

	/**
	 * The selectMap is a special case in that it is designed to convert a list
	 * of results into a Map based on one of the properties in the resulting
	 * objects.
	 * 
	 * @param <K>
	 *            the returned Map keys type
	 * @param <V>
	 *            the returned Map values type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @param mapKey
	 *            The property to use as key for each value in the list.
	 * @return Map containing key pair data.
	 */
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		return selectMap(statement, parameter, mapKey, RowBounds.DEFAULT);
	}

	/**
	 * The selectMap is a special case in that it is designed to convert a list
	 * of results into a Map based on one of the properties in the resulting
	 * objects.
	 * 
	 * @param <K>
	 *            the returned Map keys type
	 * @param <V>
	 *            the returned Map values type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @param mapKey
	 *            The property to use as key for each value in the list.
	 * @param rowBounds
	 *            Bounds to limit object retrieval
	 * @return Map containing key pair data.
	 */
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		SqlSession sqlSession = sqlSessionFactory.openSession(false);
		Map<K, V> result = null;
		try {
			result = sqlSession.selectMap(statement, parameter, mapKey, rowBounds);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * Retrieve a single row mapped from the statement
	 * using a {@code ResultHandler}.
	 * 
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param handler
	 *            ResultHandler that will handle each retrieved row
	 * @return Mapped object
	 */
	public void select(String statement, ResultHandler<?> handler) {
		select(statement, null, handler);
	}

	/**
	 * Retrieve a single row mapped from the statement key and parameter
	 * using a {@code ResultHandler}.
	 * 
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @param handler
	 *            ResultHandler that will handle each retrieved row
	 * @return Mapped object
	 */
	public void select(String statement, Object parameter, ResultHandler<?> handler) {
		select(statement, parameter, RowBounds.DEFAULT, handler);
	}

	/**
	 * Retrieve a single row mapped from the statement key and parameter
	 * using a {@code ResultHandler} and {@code RowBounds}
	 * 
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param rowBounds
	 *            RowBound instance to limit the query results
	 * @param handler
	 *            ResultHandler that will handle each retrieved row
	 * @return Mapped object
	 */
	public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler<?> handler) {
		SqlSession sqlSession = sqlSessionFactory.openSession(false);
		try {
			sqlSession.select(statement, parameter, rowBounds, handler);
		} finally {
			sqlSession.close();
		}
	}

	/**
	 * Execute an insert statement.
	 * 
	 * @param statement
	 *            Unique identifier matching the statement to execute.
	 * @return int The number of rows affected by the insert.
	 */
	public int insert(String statement) {
		return insert(statement, null);
	}

	/**
	 * Execute an insert statement with the given parameter object. Any generated
	 * autoincrement values or selectKey entries will modify the given parameter
	 * object properties. Only the number of rows affected will be returned.
	 * 
	 * @param statement
	 *            Unique identifier matching the statement to execute.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @return int The number of rows affected by the insert.
	 */
	public int insert(String statement, Object parameter) {
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		int result = 0;
		try {
			result = sqlSession.insert(statement, parameter);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * Execute an update statement. The number of rows affected will be returned.
	 * 
	 * @param statement
	 *            Unique identifier matching the statement to execute.
	 * @return int The number of rows affected by the update.
	 */
	public int update(String statement) {
		return update(statement, null);
	}

	/**
	 * Execute an update statement. The number of rows affected will be returned.
	 * 
	 * @param statement
	 *            Unique identifier matching the statement to execute.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @return int The number of rows affected by the update.
	 */
	public int update(String statement, Object parameter) {
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		int result = 0;
		try {
			result = sqlSession.update(statement, parameter);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * Execute a delete statement. The number of rows affected will be returned.
	 * 
	 * @param statement
	 *            Unique identifier matching the statement to execute.
	 * @return int The number of rows affected by the delete.
	 */
	public int delete(String statement) {
		return delete(statement, null);
	}

	/**
	 * Execute a delete statement. The number of rows affected will be returned.
	 * 
	 * @param statement
	 *            Unique identifier matching the statement to execute.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @return int The number of rows affected by the delete.
	 */
	public int delete(String statement, Object parameter) {
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		int result = 0;
		try {
			result = sqlSession.delete(statement, parameter);
		} finally {
			sqlSession.close();
		}
		return result;
	}

	/**
	 * Retrieves a mapper.
	 * 
	 * @param <T>
	 *            the mapper type
	 * @param type
	 *            Mapper interface class
	 * @return a mapper bound to this SqlSession
	 */
	public <T> T getMapper(Class<T> type) {
		SqlSession sqlSession = sqlSessionFactory.openSession(false);
		T result = null;
		try {
			result = sqlSession.getMapper(type);
		} finally {
			sqlSession.close();
		}
		return result;
	}
}
