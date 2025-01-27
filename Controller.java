/**
 *
 *  @author Janik Nikola
 *
 */

package jannik;

import java.io.*;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import zad1.models.Bind;

import java.lang.reflect.Field;
import java.lang.annotation.*;
import java.util.*;


public class Controller {
	
	private final Object modelInstance;
	private final Class<?> modelClass;
	private final Map<String, Object> variables = new HashMap<>();
	private int LL;

	public Controller(String modelName) {
		try {
			this.modelClass = Class.forName("jannik.models." + modelName);
			this.modelInstance = this.modelClass.getDeclaredConstructor().newInstance();
		} catch(Exception e) {
			throw new RuntimeException("Error initializing model: " + e.getMessage());
		}
	}
	
	public Controller readDataFrom(String fname) {
		try(BufferedReader reader = new BufferedReader(new FileReader(fname))) {
			String line;
			while((line = reader.readLine()) != null) {
				String[] tokens = line.split("\\s+");
				if(tokens[0].equalsIgnoreCase("LATA")) {
					LL = tokens.length - 1;
					variables.put("LL", LL);
					setField("LL", LL);
				} else {
					double[] values = new double[LL];
					for(int i=0; i<LL; i++) {
						values[i] = i < tokens.length - 1 ? Double.parseDouble(tokens[i+1]) : values[i-1];
					}
					variables.put(tokens[0], values);
					setField(tokens[0], values);
				}
			}
		} catch(Exception e) {
			throw new RuntimeException("Error reading data: " + e.getMessage());
		}
		return this;
		
	}
	
	public Controller runModel() {
		try {
			modelClass.getMethod("run").invoke(modelInstance);
			
			for(Field field : modelClass.getDeclaredFields()) {
				if(field.isAnnotationPresent(Bind.class)) {
					field.setAccessible(true);
					variables.put(field.getName(), field.get(modelInstance));
				}
			}
		} catch(Exception e) {
			throw new RuntimeException("Error running model: " + e.getMessage());
		}
		return this;
	}
	
	public Controller runScriptFromFile(String fname) {
		try(BufferedReader reader = new BufferedReader(new FileReader(fname))) {
			StringBuilder script = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				script.append(line).append("\n");
			}
			return runScript(script.toString());
		}catch(Exception e) {
			throw new RuntimeException("Error reading script: " + e.getMessage());
		}
	}
	

	public void runScriptFromFile(String filePath) {
		ScriptEngineManager manager = new ScriptEngineManager();
   	 	ScriptEngine engine = manager.getEngineByName("groovy");

   		 if (engine == null) {
			 throw new RuntimeException("Groovy ScriptEngine is not available. Make sure the Groovy library is included.");
    		}
		
   		for (Map.Entry<String, Object> entry : binding.getVariables().entrySet()) {
       		engine.put(entry.getKey(), entry.getValue());
    		}

    		try (FileReader reader = new FileReader(filePath)) {
       		engine.eval(reader);
    		} catch (IOException e) {
       		 	throw new RuntimeException("Error reading script file: " + filePath, e);
   		} catch (ScriptException e) {
        		throw new RuntimeException("Error running script: " + e.getMessage(), e);
    		}
	}
	
	
	public String getResultsAsTsv() {
		StringBuilder result = new StringBuilder("LATA\t");
		for(int year=2015; year<2015+LL; year++) {
			result.append(year).append("\t");
		}
		result.append("\n");
		
		variables.forEach((key, value) -> {
			result.append(key).append("\t");
			if(value instanceof double[]) {
				double[] array = (double[]) value;
				for(double v : array) {
					result.append(v).append("\t");
				}
			} else {
				result.append(value).append("\t");
			}
			result.append("\n");
		});
		
		return result.toString();
	}
	
	private void setField(String fildName, Object value) {
		try {
			Field field = modelClass.getDeclaredField(fildName);
			if(field.isAnnotationPresent(Bind.class)) {
				field.setAccessible(true);
				field.set(modelInstance, value);
			}
		} catch(NoSuchFieldException ignored) {
		} catch(Exception e) {
			throw new RuntimeException("Error setting field: " + e.getMessage());
		}
	}
}































