package be.nazarimilad.springbootgraphqlquerydsldemo.config;

import graphql.scalars.ExtendedScalars;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Component;

@Component
public class RuntimeWiringConfiguration implements RuntimeWiringConfigurer {

  @Override
  public void configure(RuntimeWiring.Builder wiringBuilder) {
    // GraphQL Java doesn't support more 'complex' data types by default (only primitive ones like Integer and String for example).
    // We're going to add support for necessary important data types such as DateTime and Long by using the wiringBuilder.
    // Note that we're not providing the definition for these GraphQL scalar types ourselve,
    // we're using the definitons provided by the 'graphql-java-extended-scalars' library
    wiringBuilder.scalar(ExtendedScalars.Date)
                 .scalar(ExtendedScalars.DateTime)
                 .scalar(ExtendedScalars.GraphQLLong)
                 .build();
  }
}
