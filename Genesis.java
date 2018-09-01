package Posist;

import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Genesis {

	public int nodeCount = 1;

	public int sum = -1;

	public String getDate() {
		Date date = new Date();

		String strDateFormat = "hh:mm:ss a";

		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

		String formattedDate = dateFormat.format(date);

		return formattedDate;

	}

	public class Node {

		Date Timestamp;

		String data = "";

		int nodeNumber = 0;

		float val = 0;
		String nodeId = "";

		String referenceNodeId = "";

		ArrayList<String> childReferenceNodeId = new ArrayList<>();

		String genesisReferenceNodeId = "";

		String HashValue = "";

		ArrayList<Node> children = new ArrayList<>();

		public Node(Node parent, float value, String ownerId, String OwnerName) {
			this.Timestamp = new Date();
			this.val = value;

			this.nodeNumber = nodeCount;
			this.nodeId = this.toString();
			if (parent != null)
				this.referenceNodeId = parent.toString();
			for (Node child : this.children) {
				this.childReferenceNodeId.add(child.toString());
			}

			String hashh = ownerId + value + OwnerName;
			String hash = hashh.hashCode() + "";
			this.data = getEncript(ownerId, value, OwnerName, hash);

			String hasho = Timestamp.toString() + data + this.nodeNumber + this.nodeId + this.referenceNodeId
					+ this.childReferenceNodeId + this.genesisReferenceNodeId;

			this.HashValue = hasho.hashCode() + "";

		}

		public String getEncript(String ownerId, float value, String ownerName, String hash) {

			try {
				String secretKey = "ssshhhhhhhhhhh!!!!";

				String codeWord = ownerId + ":" + value + ":" + ownerName + ":" + hash;
				AES as = new AES();
				String encryptedData = as.encrypt(codeWord, secretKey);

				// String decryptedData = as.decrypt(codeWord, secretKey);

				return encryptedData;

			} catch (Throwable e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	public void addNode(float value, String OwnerName, String OwnerId) throws Exception {
		addNodeHelp(value, OwnerName, OwnerId, this.genesis);
	}

	public void addNodeHelp(float value, String OwnerName, String OwnerId, Node node) throws Exception {
		if (this.genesis == null) {
			this.genesis = new Node(null, value, OwnerId, OwnerName);
			this.sum = -1;
			return;
		}
		if (value + this.sum >= this.genesis.val && sum != -1) {
			throw new Exception("value is invalid;");
		} else if (node == null) {
			node = new Node(null, value, OwnerId, OwnerName);
			if (node != this.genesis) {
				this.sum += value;
			}
		} else {
			for (int i = 0; i < genesis.children.size(); i++) {
				this.addNodeHelp(value, OwnerName, OwnerId, genesis.children.get(i));
			}
		}
	}
	
	public void edit(String nodeId,float newvalue) {
		this.genesis.val = newvalue;
	}

	public Node genesis = null;

	public class AES {

		private SecretKeySpec secretKey;
		private byte[] key;

		public void setKey(String myKey) {
			MessageDigest sha = null;
			try {
				key = myKey.getBytes("UTF-8");
				sha = MessageDigest.getInstance("SHA-1");
				key = sha.digest(key);
				key = Arrays.copyOf(key, 16);
				secretKey = new SecretKeySpec(key, "AES");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		public String encrypt(String strToEncrypt, String secret) {
			try {
				setKey(secret);
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
				return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
			} catch (Exception e) {
				System.out.println("Error while encrypting: " + e.toString());
			}
			return null;
		}

		public String decrypt(String strToDecrypt, String secret) {
			try {
				setKey(secret);
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
				return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
			} catch (Exception e) {
				System.out.println("Error while decrypting: " + e.toString());
			}
			return null;
		}
	}
}
