
public class JokeProverbState {
	

	private String name;
	boolean joke1;
	boolean joke2;
	boolean joke3;
	boolean joke4;
	boolean joke5;
	boolean proverb1;
	boolean proverb2;
	boolean proverb3;
	boolean proverb4;
	boolean proverb5;

	public JokeProverbState(String name, boolean joke1, boolean joke2, boolean joke3, boolean joke4, boolean joke5, boolean proverb1, boolean proverb2, boolean proverb3, boolean proverb4, boolean proverb5){
		name = this.name;
		joke1=false;
		joke2=false;
		joke3=false;
		joke4=false;
		joke5=false;
		proverb1=false;
		proverb2=false;
		proverb3=false;
		proverb4=false;
		proverb5=false;
	}
	public void setName(String name){
		if (name != this.name){
			name = this.name;
			clearJokes();
			clearProverbs();
			}
		}
	public void clearProverbs() {
		// TODO Auto-generated method stub
		proverb1=false;
		proverb2=false;
		proverb3=false;
		proverb4=false;
		proverb5=false;
	}
	
	public void clearJokes() {
		// TODO Auto-generated method stub
		joke1=false;
		joke2=false;
		joke3=false;
		joke4=false;
		joke5=false;
	}
	
	public void setJoke1(boolean jokeSent){joke1 = jokeSent;}
	public void setJoke2(boolean jokeSent){joke2 = jokeSent;}
	public void setJoke3(boolean jokeSent){joke3 = jokeSent;}
	public void setJoke4(boolean jokeSent){joke4 = jokeSent;}
	public void setJoke5(boolean jokeSent){joke5 = jokeSent;}
	
	public void setProverb1(boolean proverbSent){proverb1 = proverbSent;}
	public void setProverb2(boolean proverbSent){proverb2 = proverbSent;}
	public void setProverb3(boolean proverbSent){proverb3 = proverbSent;}
	public void setProverb4(boolean proverbSent){proverb4 = proverbSent;}
	public void setProverb5(boolean proverbSent){proverb5 = proverbSent;}
	
	
	public boolean checkIfJokesComplete(){
		return (joke1 && joke2 && joke3 && joke4 && joke5);
	}
	
	public boolean checkIfProverbsComplete(){
		return (proverb1 && proverb2 && proverb3 && proverb4 && proverb5);
	}



}
