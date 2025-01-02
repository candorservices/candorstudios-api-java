package net.candorstudios.api.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a <a href="https://candorstudios.notion.site/Remote-Configs-16e9e5d3cd0f807a9ce3eb679a3d3c63?pvs=4">Remote Config</a> accessed from the Candor Studios API.
 * <br>Remote Configs are an easier, web-based configuration system that allows your client to easily update configurations without needing to change code or config files.
 * <p>GET /configs/:id/retrieve-values returns a JSON object with the values of the config, which this class uses to decode.
 * @author George
 * @version 1.0.0
 */
public class CandorRemoteConfig {

    private final Map<String, Object> values;
    private final JSONObject json;

    public CandorRemoteConfig(@NotNull JSONObject in) {
        this.values = in.toMap();
        this.json = in;
    }

    @NotNull
    public Map<String, Object> getValues() {
        return values;
    }

    /**
     * This method attempts to replicate the behavior of YAML or JS objects. Basically, you can access a value in any specific bit by using
     * a dot (.) to separate the keys. For example, if you have a config that looks like this:
     * <pre>
     *{
     *     "test": {
     *      "test2": {
     *          "test3": "Hello World!"
     *      }
     *    }
     * }
     *</pre>
     *
     * and you wanted to get the value of test3, you would use the following code:
     * <pre>
     *     Config config = new Config(json);
     *     String test3 = config.get("test.test2.test3");
     *     System.out.println(test3);
     *     // Output: Hello World!
     *  </pre>
     *
     *  You can also use this method to get values from arrays. For example, if you have a config that looks like this:
     *  <pre>
     *      {
     *          "test": {
     *              "test2": {
     *                  "test3": [
     *                      {
     *                          "test4": "Hello World!"
     *                      }
     *                  ]
     *               }
     *          }
     *      }
     * </pre>
     *
     * and you wanted to get the value of test4, you would use the following code:
     * <pre>
     *     RemoteConfig config = new RemoteConfig(json);
     *     String test4 = config.get("test.test2.test3.0.test4");
     *     System.out.println(test4);
     *     // Output: Hello World!
     * </pre>
     */
    @Nullable
    public Object get(String key) {
        String[] split = key.split("\\.");

        Map<String, Object> values = this.values;
        List<Map<String, Object>> listValues = null;
        int index = 0;
        for (String s : split) {
            index++;
            if (values.get(s) instanceof Map) {
                values = (Map<String, Object>) values.get(s);
            } else if (values.get(s) instanceof List) {
                listValues = (List<Map<String, Object>>) values.get(s);
                int listValuesIndex = Integer.parseInt(split[index]);
                values = listValues.get(listValuesIndex);
            }
        }

        return values.get(split[split.length - 1]);
    }

    @Nullable
    public String getString(String key) {
        return (String) get(key);
    }

    public int getInt(String key) {
        return (int) get(key);
    }

    public boolean getBoolean(String key) {
        return (boolean) get(key);
    }

    public double getDouble(String key) {
        return (double) get(key);
    }

    public float getFloat(String key) {
        return (float) get(key);
    }

    public long getLong(String key) {
        return (long) get(key);
    }

    @Nullable
    public List<HashMap<String, Object>> getList(String key) {
        return (List<HashMap<String, Object>>) values.get(key);
    }

    @Nullable
    public HashMap<String, Object> getObject(String key) {
        return (HashMap<String, Object>) values.get(key);
    }


    /**
     * You can also just retrieve the raw JSON object, if you prefer.
     */
    public JSONObject getJson() {
        return json;
    }
}
