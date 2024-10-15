package memo.strategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * 하이버네이트 사용시 Entity 필드와 DB 컬럼 네이밍 전략 Entity 파스칼케이스 <-> DB 스네이크케이스
 */
public class SnakeCasePhysicalNamingStrategy implements PhysicalNamingStrategy {

	@Override
	public Identifier toPhysicalCatalogName(Identifier var1, JdbcEnvironment var2) {
		
		return convertToSnakeCase(var1);
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier var1, JdbcEnvironment var2) {
		
		return convertToSnakeCase(var1);
	}

	@Override
	public Identifier toPhysicalSchemaName(Identifier var1, JdbcEnvironment var2) {
		
		return convertToSnakeCase(var1);
	}

	@Override
	public Identifier toPhysicalSequenceName(Identifier var1, JdbcEnvironment var2) {
		
		return convertToSnakeCase(var1);
	}

	@Override
	public Identifier toPhysicalTableName(Identifier var1, JdbcEnvironment var2) {

		return convertToSnakeCase(var1);
	}
	
	private Identifier convertToSnakeCase(Identifier identifier) {
		
        if (identifier == null) {
            return null;
        }
        String name = identifier.getText();
        String newName = name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        return Identifier.toIdentifier(newName);
    }

}
