package converters;

import domain.File;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import repositories.FileRepository;

import javax.transaction.Transactional;

@Component
@Transactional
public class StringToFileConverter implements Converter<String, File>{
	

	@Autowired
	private FileRepository repository;
	
	@Override
	public File convert(String text) {
		File result;
		int id;		
		try {
			if(StringUtils.isEmpty(text)){
				result = null;
			}else{
				id = Integer.valueOf(text);
				result = this.repository.findOne(id);
			}
		} catch(Throwable oops) {
			throw new IllegalArgumentException(oops);
		}		
		return result;
	}	
}
