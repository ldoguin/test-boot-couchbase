package org.couchbase.devex;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.util.rawQuerying.RawQueryExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Autowired
	private Bucket bucket;

	@Bean
	CommandLineRunner init() {
		return (String[] args) -> {
			System.out.println(bucket.name());
		};
	}


	@Bean
	public RawQueryExecutor rawQueryExecutor(final Bucket bucket){
		return  new RawQueryExecutor(bucket.name(), "", bucket.core(), bucket.environment());
	}
}

@Controller
class IndexController {

	@Autowired
	private Bucket bucket;

	@RequestMapping("/query/")
	@ResponseBody
	public String doQuery(RawQueryExecutor rawQueryExecutor) {
		return rawQueryExecutor.n1qlToRawJson(N1qlQuery.simple("SELECT * FROM " + bucket.name()));
	}

}