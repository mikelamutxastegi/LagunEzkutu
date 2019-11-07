import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Main {


	private static final String EMAIL_ZERRENDA = "korreuek.txt";
	private static final String EMAIL_TESTUA = "testua.txt";


	public static void main(String[] args) {
		Main main = new Main();
		List<String> emailIzen = null;
		try {
			emailIzen = main.emailakKargatu();
			String[][] emailIzenAukera = main.aukeraketaEgin(emailIzen);
			main.emailaBidali(emailIzenAukera);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public List<String> emailakKargatu() throws URISyntaxException, IOException {
		Path emailKokalekua = Paths.get(EMAIL_ZERRENDA);
		List<String> emailIzen = Files.readAllLines(emailKokalekua);
		return emailIzen;
	}

	public String[][] aukeraketaEgin(List<String> emailIzen) {
		Random aukeraketa = new Random();
		String[][] emailIzenAukera = new String[emailIzen.size()][2];
		List<String> emailIzenKopia = new ArrayList<>(emailIzen);

		for(int i = 0; i < emailIzen.size(); i++) {
			emailIzenAukera[i][0] = emailIzen.get(i);
			int aukeraZbk = aukeraketa.nextInt(emailIzenKopia.size());
			if(emailIzenAukera[i][0].equals(emailIzenKopia.get(aukeraZbk))) {
				i--;
			}else{
				emailIzenAukera[i][1] = emailIzenKopia.get(aukeraZbk);
				emailIzenKopia.remove(aukeraZbk);
			}
		}

		return emailIzenAukera;
	}

	public void emailaBidali(String[][] emailIzenAukera) throws IOException {
		String nork = "l.ezkutu@gmail.com";
		String pasahitza = "5603864cc10f117a812158d3c4d5ba6a71e2e98ba53c1bc81309709176a2def1";
		Properties propietateak = System.getProperties();
		
		propietateak.put("mail.smtp.auth", "true");
		propietateak.put("mail.smtp.starttls.enable", "true");
		propietateak.put("mail.smtp.host", "smtp.gmail.com");
		propietateak.put("mail.smtp.port", "587");

		Session session = Session.getInstance(propietateak,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(nork, pasahitza);
			}
		});
		String emailTestua = emailarenTestuaKargatu();
		String emailTestuaKopia = emailTestua;
		for(int i = 0; i < emailIzenAukera.length; i++) {
			String emaila = emailIzenAukera[i][0].split(",")[0];
			String aukeratua = emailIzenAukera[i][1].split(",")[1];
			try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(nork));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(emaila));
				message.setSubject("Lagun Ezkutua 2019-2020");
				emailTestuaKopia = emailTestua.replaceAll("%", aukeratua);
				message.setText(emailTestuaKopia);
				Transport.send(message);
				System.out.println("Mezua bidalita");
			} catch (MessagingException mex) {
				mex.printStackTrace();
			}
		}
	}

	public String emailarenTestuaKargatu() throws IOException {
		Path emailKokalekua = Paths.get(EMAIL_TESTUA);
		String emailTestua = new String(Files.readAllBytes(emailKokalekua));
		return emailTestua;
	}

}
