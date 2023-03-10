import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class Config {
    // log4j logging
    private static final Logger logger = LogManager.getLogger(Config.class);

    // Singleton
    private static Config instance;

    // Discord bot token
    private final String botToken;

    // OpenaAi token
    private final String gptToken;

    // OpenAi Configuration for requests
    private String openAiModel;
    private final Integer openAiMaxTokens;
    private  Double openAiTemperature;
    private final Character commandCharacter;

    private final String openAiImageSize;

    private final String openAiImageResponseFormat;

    private final String nonComplianceBotReply;

    private final String requestFailureBotReply;

    private final String botPersonality;

    private final Integer apiTimeout;


    private static final String DEFAULT_RESPONSE_FAILURE = "A failure to retrieve a response occurred, " +
            "please try again or add more details to your query.";

    private static final String DEFAULT_BOT_PERSONALITY = "you are a rude discord bot, and will never apologize " +
            "or be polite. Always answer questions factually, with a rude comment back at the user";

    private static final int DEFAULT_MAX_TOKENS = 1000;

    private static final  String DEFAULT_IMAGE_SIZE = "1024x1024";

    private static final char DEFAULT_COMMAND_CHAR = '!';

    private static final double DEFAULT_TEMPERATURE = 0.7D;

    private static final String DEFAULT_NON_COMPLIANCE_MESSAGE = "Sorry request does not comply with" +
            " OpenAI's Content Policy";

    private static final String DEFAULT_MODEL = "gpt-3.5-turbo";

    private static final int DEFAULT_API_TIMEOUT = 20;

    private Config() {
        try {
            Properties config = new Properties();
            String dir = new File(BotApp.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath();
            config.load(new FileInputStream(dir + "/config.properties"));
            botToken = config.getProperty("botToken");
            gptToken = config.getProperty("gptToken");
            commandCharacter = config.getProperty("commandCharacter").charAt(0);
            openAiMaxTokens = Integer.parseInt(config.getProperty("openAiMaxTokens"));
            openAiModel = config.getProperty("openAiModel");
            openAiTemperature = Double.parseDouble(config.getProperty("openAiTemperature"));
            openAiImageSize = config.getProperty("openAiImageSize");
            openAiImageResponseFormat = config.getProperty("openAiImageResponseFormat");
            nonComplianceBotReply = config.getProperty("nonComplianceBotReply");
            requestFailureBotReply = config.getProperty("requestFailureBotReply");
            botPersonality = config.getProperty("botPersonality");
            apiTimeout = Integer.parseInt(config.getProperty("apiTimeout"));
        } catch (IOException | URISyntaxException e) {
            logger.error("exception occured: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return discord bot reply, when a response could not be retrieved from openAI
     */
    public String getRequestFailureBotReply() {
        return requestFailureBotReply == null ? DEFAULT_RESPONSE_FAILURE : requestFailureBotReply;
    }

    /**
     *
     * @return discord bot reply, when prompt does not comply with OpenAi policy
     */
    public String getNonComplianceBotReply() {
        return nonComplianceBotReply == null ? DEFAULT_NON_COMPLIANCE_MESSAGE : nonComplianceBotReply;
    }

    /**
     *
     * @return response format of openAi image request
     */
    public String getOpenAiImageResponseFormat() {
        return openAiImageResponseFormat == null ? DEFAULT_IMAGE_SIZE : openAiImageResponseFormat;
    }

    /**
     *
     * @return how large of an image will open AI generate
     */
    public String getOpenAiImageSize() {
        return openAiImageSize == null ? DEFAULT_IMAGE_SIZE : openAiImageSize;
    }

    /**
     *
     * @return current OpenAi temperature being used for requests
     */
    public double getOpenAiTemperature() {
        return openAiTemperature == null ? DEFAULT_TEMPERATURE : openAiTemperature;
    }

    /**
     *
     * @param temperature OpenAi temperature to be used
     */
    public void setOpenAiTemperature(double temperature) {
        openAiTemperature = temperature;
    }

    /**
     *
     * @param model OpenAi Model to be used
     */

    public void setOpenAiModel(String model) {
        openAiModel = model;
    }

    /**
     *
     * @return current OpenAi Model in use
     */
    public String getOpenAiModel() {
        return openAiModel == null ? DEFAULT_MODEL : openAiModel;
    }

    /**
     *
     * @return Max tokens allowed for OpenAi Response
     */
    public int getOpenAiMaxTokens() {
        return openAiMaxTokens == null ? DEFAULT_MAX_TOKENS : openAiMaxTokens;
    }

    /**
     *
     * @return Discord Token
     */
    public String getBotToken() {
        return botToken;
    }

    /**
     *
     * @return OpenAi Token
     */
    public String getGptToken() {
        return gptToken;
    }

    /**
     *
     * @return Character to indicate bot command
     */
    public char getCommandCharacter() {
        return commandCharacter == null ? DEFAULT_COMMAND_CHAR : commandCharacter;
    }

    /**
     *
     * @return A description passed to openAi, to affect how the bot responds
     */
    public String getBotPersonality() {
        return botPersonality == null ? DEFAULT_BOT_PERSONALITY : botPersonality;
    }

    /**
     *
     * @return time in seconds to wait on openAi request
     */
    public int getApiTimeout() {
        return apiTimeout == null ? DEFAULT_API_TIMEOUT : apiTimeout;
    }

    /**
     *
     * @return config instance
     */
    public static Config getInstance() {
        return instance == null ? (instance = new Config()): instance;
    }

    @Override
    public String toString() {
        String configuration = "";
        configuration += "\tRandomness:  " + getOpenAiTemperature() + "\n";
        configuration += "\tToken Limit: " + getOpenAiMaxTokens() + "\n";
        configuration += "\tAi Model:    " + getOpenAiModel() + "\n";
        return configuration;
    }
}
