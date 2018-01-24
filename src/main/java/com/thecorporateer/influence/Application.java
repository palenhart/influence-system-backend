package com.thecorporateer.influence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication(scanBasePackages = { "com.thecorporateer.influence" })
@EnableScheduling
public class Application {

//	@Autowired
//	private UserHandlingService userHandlingService;
//	@Autowired
//	private ObjectService objectService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener(ContextRefreshedEvent.class)
	public void initialize() {

		// create users for this phase
		// userHandlingService.createTestuser("weyland", "Pete", "P%bm5xb3ZA", true);
		// corporateerHandlingService.setRank("Pete", "CEO");
		// userHandlingService.createTestuser("zollak", "Zollak", "tX&7E§rtAF", true);
		// corporateerHandlingService.setRank("Zollak", "Board Member");
		// userHandlingService.createTestuser("braden12", "braden12", "9cNQqr9$uT",
		// true);
		// corporateerHandlingService.setRank("braden12", "Board Member");
		// userHandlingService.createTestuser("nikmyth", "Nikmyth", "zKLidnH1!6", true);
		// corporateerHandlingService.setRank("Nikmyth", "Board Member");
		// corporateerHandlingService.electionWorkaroundChangeCorporateerDivisionMembership("braden12",
		// "Transport", true);
//		Division transport = objectService.getDivisionByName("Transport");
//		Division trade = objectService.getDivisionByName("Trade");
//		Division development = objectService.getDivisionByName("Development");
//		Division extraction = objectService.getDivisionByName("Extraction");
//		
//		List<String> all = new ArrayList<String>(Arrays.asList("Transport", "Extraction", "Development", "Trade"));
//		List<String> noTrade = new ArrayList<String>(Arrays.asList("Transport", "Extraction", "Development"));
//		List<String> noDevelopment = new ArrayList<String>(Arrays.asList("Transport", "Extraction", "Trade"));
//		List<String> TransportExtraction = new ArrayList<String>(Arrays.asList("Transport", "Extraction"));
//		List<String> TransportDevelopment = new ArrayList<String>(Arrays.asList("Transport", "Development"));
//		List<String> TransportTrade = new ArrayList<String>(Arrays.asList("Transport", "Trade"));
//		List<String> Transport = new ArrayList<String>(Arrays.asList("Transport"));
//		List<String> Extraction = new ArrayList<String>(Arrays.asList("Extraction"));
//		List<String> Development = new ArrayList<String>(Arrays.asList("Development"));
//		List<String> Trade = new ArrayList<String>(Arrays.asList("Trade"));
//		List<String> ExtractionTrade = new ArrayList<String>(Arrays.asList("Extraction", "Trade"));
//		List<String> ExtractionDevelopment = new ArrayList<String>(Arrays.asList("Extraction", "Development"));
//		
//
//		userHandlingService.createUser("Weyland", trade, all);
//		userHandlingService.createUser("Zollak", extraction, Extraction);
//		userHandlingService.createUser("Braden12", trade, TransportTrade);
//		
//		userHandlingService.createUser("Advantys287", trade, all);
//		userHandlingService.createUser("Indigo Blue Bodolf", trade, all);
//		userHandlingService.createUser("Crump", transport, noTrade);
//		userHandlingService.createUser("dankie", transport, noTrade);
//		userHandlingService.createUser("DEFixt05", transport, noTrade);
//		userHandlingService.createUser("Icestorm", transport, noTrade);
//		userHandlingService.createUser("Mog_No_1", transport, noTrade);
//		userHandlingService.createUser("RotorBoy", transport, noTrade);
//		userHandlingService.createUser("xSTRYKERx", transport, noTrade);
//		userHandlingService.createUser("GingaNinjaNZ", trade, noDevelopment);
//		userHandlingService.createUser("One Million Dongers", trade, noDevelopment);
//		userHandlingService.createUser("Wildrunner", trade, noDevelopment);
//		userHandlingService.createUser("Blue", transport, TransportExtraction);
//		userHandlingService.createUser("CalypsoMoon", transport, TransportExtraction);
//		userHandlingService.createUser("Dark_Angel_91", transport, TransportExtraction);
//		userHandlingService.createUser("DaUndeadSamurai(rotten/Aerios)", transport, TransportExtraction);
//		userHandlingService.createUser("Franx", transport, TransportExtraction);
//		userHandlingService.createUser("Ghost/Drake", transport, TransportExtraction);
//		userHandlingService.createUser("graypay", transport, TransportExtraction);
//		userHandlingService.createUser("HaliHunter", transport, TransportExtraction);
//		userHandlingService.createUser("ieflyingace", transport, TransportExtraction);
//		userHandlingService.createUser("Katadolon", transport, TransportExtraction);
//		userHandlingService.createUser("Nomad_X", transport, TransportExtraction);
//		userHandlingService.createUser("Revoxxer", transport, TransportExtraction);
//		userHandlingService.createUser("Sputnik", transport, TransportExtraction);
//		userHandlingService.createUser("Alestair", transport, TransportDevelopment);
//		userHandlingService.createUser("bones5", transport, TransportDevelopment);
//		userHandlingService.createUser("Cowdera", transport, TransportDevelopment);
//		userHandlingService.createUser("ScheduledTiger", transport, TransportDevelopment);
//		userHandlingService.createUser("Shaded", transport, TransportDevelopment);
//		userHandlingService.createUser("Zentuz", transport, TransportDevelopment);
//		userHandlingService.createUser("Aaron565", trade, TransportTrade);
//		userHandlingService.createUser("Banu_Merchant", trade, TransportTrade);
//		userHandlingService.createUser("Bunnybutcher", trade, TransportTrade);
//		userHandlingService.createUser("Chippy", trade, TransportTrade);
//		userHandlingService.createUser("Granothon", trade, TransportTrade);
//		userHandlingService.createUser("Neeners", trade, TransportTrade);
//		userHandlingService.createUser("NovaStella", trade, TransportTrade);
//		userHandlingService.createUser("Rabidmellor", trade, TransportTrade);
//		userHandlingService.createUser("Siserone", trade, TransportTrade);
//		userHandlingService.createUser("SoreGums", trade, TransportTrade);
//		userHandlingService.createUser("speechless", trade, TransportTrade);
//		userHandlingService.createUser("[S2] Diablokiller", transport, Transport);
//		userHandlingService.createUser("2 Certs", transport, Transport);
//		userHandlingService.createUser("AdmiralTrench", transport, Transport);
//		userHandlingService.createUser("anotherpug", transport, Transport);
//		userHandlingService.createUser("Arc", transport, Transport);
//		userHandlingService.createUser("Arie - MoeGril", transport, Transport);
//		userHandlingService.createUser("Azeel", transport, Transport);
//		userHandlingService.createUser("Badger 1-1", transport, Transport);
//		userHandlingService.createUser("Badwhisky", transport, Transport);
//		userHandlingService.createUser("BakedPotato", transport, Transport);
//		userHandlingService.createUser("baronbonbon", transport, Transport);
//		userHandlingService.createUser("Barrakudah", transport, Transport);
//		userHandlingService.createUser("benpast", transport, Transport);
//		userHandlingService.createUser("Berko", transport, Transport);
//		userHandlingService.createUser("BumBumSneeze", transport, Transport);
//		userHandlingService.createUser("Ceo", transport, Transport);
//		userHandlingService.createUser("CherryTree", transport, Transport);
//		userHandlingService.createUser("ckobler", transport, Transport);
//		userHandlingService.createUser("CMDR_Curgon", transport, Transport);
//		userHandlingService.createUser("Crane228", transport, Transport);
//		userHandlingService.createUser("Cy3eria", transport, Transport);
//		userHandlingService.createUser("Da-Ku", transport, Transport);
//		userHandlingService.createUser("Darthow", transport, Transport);
//		userHandlingService.createUser("DAVROSS", transport, Transport);
//		userHandlingService.createUser("Diggerman62", transport, Transport);
//		userHandlingService.createUser("DIMM123", transport, Transport);
//		userHandlingService.createUser("D̸̂̈ͯE̟̫ͦ̑ͩV̧̫̖̟̾ͨͤ1͐̾̀AT̘͊͐̂̑͟E", transport, Transport);
//		userHandlingService.createUser("EdwardCromwell", transport, Transport);
//		userHandlingService.createUser("EElectranovaZA", transport, Transport);
//		userHandlingService.createUser("Emson079", transport, Transport);
//		userHandlingService.createUser("FATFINGERFINESSE", transport, Transport);
//		userHandlingService.createUser("GreyShade", transport, Transport);
//		userHandlingService.createUser("Grizeen", transport, Transport);
//		userHandlingService.createUser("GunsNGlue", transport, Transport);
//		userHandlingService.createUser("gusgusHH", transport, Transport);
//		userHandlingService.createUser("Interitus", transport, Transport);
//		userHandlingService.createUser("IonicEquilibria", transport, Transport);
//		userHandlingService.createUser("Ixtyr", transport, Transport);
//		userHandlingService.createUser("JDLG", transport, Transport);
//		userHandlingService.createUser("Jetmech", transport, Transport);
//		userHandlingService.createUser("Joe", transport, Transport);
//		userHandlingService.createUser("John Profit", transport, Transport);
//		userHandlingService.createUser("JoshuaF", transport, Transport);
//		userHandlingService.createUser("Jovian", transport, Transport);
//		userHandlingService.createUser("K95", transport, Transport);
//		userHandlingService.createUser("Kameryl", transport, Transport);
//		userHandlingService.createUser("Karazantor", transport, Transport);
//		userHandlingService.createUser("kepekliekmek", transport, Transport);
//		userHandlingService.createUser("KhronicalTV", transport, Transport);
//		userHandlingService.createUser("kitomer", transport, Transport);
//		userHandlingService.createUser("kuhioday", transport, Transport);
//		userHandlingService.createUser("Ladyalyth", transport, Transport);
//		userHandlingService.createUser("LejothOre", transport, Transport);
//		userHandlingService.createUser("LooseHalo", transport, Transport);
//		userHandlingService.createUser("Lossless", transport, Transport);
//		userHandlingService.createUser("mac14usmc", transport, Transport);
//		userHandlingService.createUser("midnight_storm1", transport, Transport);
//		userHandlingService.createUser("Palasz", transport, Transport);
//		userHandlingService.createUser("Pali (R-Rattus)", transport, Transport);
//		userHandlingService.createUser("Pantha", transport, Transport);
//		userHandlingService.createUser("PURE_Vengeance", transport, Transport);
//		userHandlingService.createUser("Pyrothic", transport, Transport);
//		userHandlingService.createUser("Radipole", transport, Transport);
//		userHandlingService.createUser("Raven Nóttdis", transport, Transport);
//		userHandlingService.createUser("Razor", transport, Transport);
//		userHandlingService.createUser("Scorpiodragon", transport, Transport);
//		userHandlingService.createUser("Sgt_Bob", transport, Transport);
//		userHandlingService.createUser("SgtPeps", transport, Transport);
//		userHandlingService.createUser("Shodo", transport, Transport);
//		userHandlingService.createUser("shooterjennings", transport, Transport);
//		userHandlingService.createUser("Sights2012", transport, Transport);
//		userHandlingService.createUser("Slimey2", transport, Transport);
//		userHandlingService.createUser("SNoRKiNG", transport, Transport);
//		userHandlingService.createUser("Storm", transport, Transport);
//		userHandlingService.createUser("SUPPRESS0R", transport, Transport);
//		userHandlingService.createUser("TheGanjaFarmer", transport, Transport);
//		userHandlingService.createUser("TheIceCreamTroll", transport, Transport);
//		userHandlingService.createUser("Thundagar", transport, Transport);
//		userHandlingService.createUser("Titan", transport, Transport);
//		userHandlingService.createUser("TrueXPixels", transport, Transport);
//		userHandlingService.createUser("VAurelius", transport, Transport);
//		userHandlingService.createUser("ViirdReytal", transport, Transport);
//		userHandlingService.createUser("VirtualDMN42", transport, Transport);
//		userHandlingService.createUser("vLyrinx", transport, Transport);
//		userHandlingService.createUser("VoidSlicer", transport, Transport);
//		userHandlingService.createUser("Vyryn", transport, Transport);
//		userHandlingService.createUser("W00kie98", transport, Transport);
//		userHandlingService.createUser("WalBao", transport, Transport);
//		userHandlingService.createUser("Wild Dog", transport, Transport);
//		userHandlingService.createUser("WindWalker", transport, Transport);
//		userHandlingService.createUser("Wishagon", transport, Transport);
//		userHandlingService.createUser("Xane", transport, Transport);
//		userHandlingService.createUser("Xelros", transport, Transport);
//		userHandlingService.createUser("Zilocke", transport, Transport);
//		userHandlingService.createUser("ゆぐ＠マサムネ鯖", transport, Transport);
//		userHandlingService.createUser("Teefy", extraction, ExtractionDevelopment);
//		userHandlingService.createUser("Dracoflame", trade, ExtractionTrade);
//		userHandlingService.createUser("galactiphat", trade, ExtractionTrade);
//		userHandlingService.createUser("alexplayer", extraction, Extraction);
//		userHandlingService.createUser("BeNNee", extraction, Extraction);
//		userHandlingService.createUser("Cevranath", extraction, Extraction);
//		userHandlingService.createUser("C̸R̷i̸S̸i̴S̴i̵D̸E̸N̵T̶i̷T̷Y̷", extraction, Extraction);
//		userHandlingService.createUser("Daniel", extraction, Extraction);
//		userHandlingService.createUser("Darth Cemí", extraction, Extraction);
//		userHandlingService.createUser("Exile", extraction, Extraction);
//		userHandlingService.createUser("LordMaligant", extraction, Extraction);
//		userHandlingService.createUser("LostRaider", extraction, Extraction);
//		userHandlingService.createUser("Mathiesin", extraction, Extraction);
//		userHandlingService.createUser("Sil", extraction, Extraction);
//		userHandlingService.createUser("Storacle", extraction, Extraction);
//		userHandlingService.createUser("Tsunamisan", extraction, Extraction);
//		userHandlingService.createUser("coreclan", development, Development);
//		userHandlingService.createUser("Inanedragon3", development, Development);
//		userHandlingService.createUser("Sean Ryan", development, Development);
//		userHandlingService.createUser("Sinnistrad", development, Development);
//		userHandlingService.createUser("Adrian Beil", trade, Trade);
//		userHandlingService.createUser("Alex-needs-a-sandwich", trade, Trade);
//		userHandlingService.createUser("average.panda", trade, Trade);
//		userHandlingService.createUser("Cal", trade, Trade);
//		userHandlingService.createUser("Centurion", trade, Trade);
//		userHandlingService.createUser("Ed_5", trade, Trade);
//		userHandlingService.createUser("Hextross", trade, Trade);
//		userHandlingService.createUser("Jacob Owl", trade, Trade);
//		userHandlingService.createUser("Jurgis", trade, Trade);
//		userHandlingService.createUser("Khonsu", trade, Trade);
//		userHandlingService.createUser("KingCarter", trade, Trade);
//		userHandlingService.createUser("Menethil", trade, Trade);
//		userHandlingService.createUser("Zarru", trade, Trade);
//		userHandlingService.createUser("Zwilahr", trade, Trade);

	}

	// Going to application.properties and setting log level of
	// logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter
	// to DEBUG activates logging of every request
	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
		CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
		loggingFilter.setIncludeClientInfo(true);
		loggingFilter.setIncludeQueryString(true);
		loggingFilter.setIncludePayload(true);
		return loggingFilter;
	}
}
