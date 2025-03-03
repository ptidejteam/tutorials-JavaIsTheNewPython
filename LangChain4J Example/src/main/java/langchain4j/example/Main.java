package langchain4j.example;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;

public class Main {
	private static final String API_KEY = "demo";

	public static void main(final String[] args) {
		OpenAiChatModel model = OpenAiChatModel.builder().apiKey(Main.API_KEY)
				.modelName(OpenAiChatModelName.GPT_4_O_MINI).build();

		String answer = model.chat("Give me the advantages and disadvantages of Java");
		System.out.println(answer);
	}
}
