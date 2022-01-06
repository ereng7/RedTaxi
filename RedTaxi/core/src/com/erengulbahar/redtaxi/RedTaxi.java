package com.erengulbahar.redtaxi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;


public class RedTaxi extends ApplicationAdapter
{
	SpriteBatch batch;
	Texture background;
	Texture taxi;
	Texture yellowcar;
	Texture bluecar;
	Texture greencar;
	Texture shadow;
	int score = 0;
	float taxiX = 0;
	float taxiY = 0;
	int gameState = 0;
	int numberOfEnemies = 4;
	int scoredEnemy = 0;
	int highScore = 0;
	int state=0;
	float temp;
	float moveX = 0;
	float moveY = 0;
	float info = 0;
	float infoY = 0;
	float infoX = 0;
	float distance = 0;
	float enemyVelocity = 10;
	float secondY;
	float [] enemyY = new float[numberOfEnemies];
	float [] enemyX = new float[numberOfEnemies];
	String sScore;
	String sHighScore;
	Circle[] enemyCircles;
	Circle[] enemyCircles2;
	Circle[] enemyCircles3;
	Circle taxiCircle;
	ShapeRenderer shapeRenderer;
	BitmapFont font;
	BitmapFont font2;
	BitmapFont font3;
	Preferences prefs;
	Sprite sprite;
	Music driveSound;
	Music jumpSound;
	Music swipeSound;
	
	@Override
	public void create ()
	{
		batch = new SpriteBatch();

		background = new Texture("background.png");
		taxi = new Texture("taxi.png");
		yellowcar = new Texture("yellow.png");
		bluecar = new Texture("blue.png");
		greencar = new Texture("green.png");
		shadow = new Texture("shadow.png");

		prefs = Gdx.app.getPreferences("game preferences");

		sprite = new Sprite(taxi);

		driveSound = Gdx.audio.newMusic(Gdx.files.internal("driveCar.mp3"));
		jumpSound = Gdx.audio.newMusic(Gdx.files.internal("jumpCar.mp3"));
		swipeSound = Gdx.audio.newMusic(Gdx.files.internal("swipeCar.mp3"));

		Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {
			@Override
			public void onLeft()
			{
				if(info==taxiX)
				{
					swipeSound.play();
					moveX = (-Gdx.graphics.getWidth()/3.08f)/25.0f;
					infoX=24;
					state=4;
				}

				else if(info==taxiX+Gdx.graphics.getWidth()/3.08f)
				{
					swipeSound.play();
					temp=Gdx.graphics.getWidth()/3.08f;
					infoX=24;
					state=3;
				}
			}

			@Override
			public void onRight()
			{
				if(info==taxiX)
				{
					swipeSound.play();
					moveX = (Gdx.graphics.getWidth()/3.08f)/25.0f;
					infoX=24;
					state=2;
				}

				else if(info==taxiX-Gdx.graphics.getWidth()/3.08f)
				{
					swipeSound.play();
					temp=-Gdx.graphics.getWidth()/3.08f;
					infoX=24;
					state=1;
				}
			}

			@Override
			public void onUp()
			{
				if(infoY==taxiY || infoY<=taxiY/3)
				{
					jumpSound.play();
					moveY = Gdx.graphics.getWidth()/17+Gdx.graphics.getHeight()/4.32f;
				}

			}

			@Override
			public void onDown()
			{

			}
		}));

		taxiX = Gdx.graphics.getWidth()/3 + Gdx.graphics.getWidth()/15.4f;
		taxiY = Gdx.graphics.getHeight()/43 + Gdx.graphics.getHeight()/14.4f;
		distance = Gdx.graphics.getHeight()/2 + Gdx.graphics.getHeight()/12.7f;
		info = taxiX + moveX;

		taxiCircle = new Circle();
		enemyCircles = new Circle[numberOfEnemies];
		enemyCircles2 = new Circle[numberOfEnemies];
		enemyCircles3 = new Circle[numberOfEnemies];

		shapeRenderer = new ShapeRenderer();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(Gdx.graphics.getWidth()/275);

		font2 = new BitmapFont();
		font2.setColor(Color.WHITE);
		font2.getData().setScale(Gdx.graphics.getWidth()/216);

		font3 = new BitmapFont();
		font3.setColor(Color.WHITE);
		font3.getData().setScale(Gdx.graphics.getWidth()/270);

		for(int i=0;i<numberOfEnemies;i++)
		{
			enemyY[i] = Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/14.4f + i*distance;
			enemyX[i] = Gdx.graphics.getWidth()/3 + Gdx.graphics.getWidth()/15.4f;

			enemyCircles[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();
		}

	}

	@Override
	public void render ()
	{
		batch.begin();

		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		sprite.draw(batch);
		sprite.setSize(Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/15);
		sprite.setPosition(taxiX+moveX,taxiY+moveY);

		secondY = taxiY+moveY;
		info = taxiX + moveX;
		infoY = taxiY + moveY;

		if(moveY != 0)
		{
			moveY = moveY - moveY/3.7f;
		}

		if(moveY>=(Gdx.graphics.getWidth()/17+Gdx.graphics.getHeight()/4.32f)/100)
		{
			batch.draw(shadow,taxiX+moveX,taxiY,Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/40);
		}

		if(infoX!=0 && state==1)
		{
			temp = temp + (Gdx.graphics.getWidth()/3.08f)/25.0f;
			moveX = temp;

			if(infoX==1)
			{
				moveX = 0;
			}

			if(infoX==24)
			{
				sprite.setRotation(-48);
			}

			else if(infoX==21)
			{
				sprite.setRotation(-42);
			}

			else if(infoX==18)
			{
				sprite.setRotation(-36);
			}

			else if(infoX==15)
			{
				sprite.setRotation(-30);
			}

			else if(infoX==12)
			{
				sprite.setRotation(-24);
			}

			else if(infoX==9)
			{
				sprite.setRotation(-6);
			}

			else if(infoX==6)
			{
				sprite.setRotation(0);
			}

			infoX--;
		}

		if(infoX!=0 && state==2)
		{
			moveX = moveX + (Gdx.graphics.getWidth()/3.08f)/25.0f;

			if(infoX==1)
			{
				moveX=Gdx.graphics.getWidth()/3.08f;
			}

			if(infoX==24)
			{
				sprite.setRotation(-48);
			}

			else if(infoX==21)
			{
				sprite.setRotation(-42);
			}

			else if(infoX==18)
			{
				sprite.setRotation(-36);
			}

			else if(infoX==15)
			{
				sprite.setRotation(-30);
			}

			else if(infoX==12)
			{
				sprite.setRotation(-24);
			}

			else if(infoX==9)
			{
				sprite.setRotation(-6);
			}

			else if(infoX==6)
			{
				sprite.setRotation(0);
			}

			infoX--;
		}

		if(infoX!=0 && state==3)
		{
			temp = temp - (Gdx.graphics.getWidth()/3.08f)/25.0f;
			moveX = temp;

			if(infoX==1)
			{
				moveX = 0;
			}

			if(infoX==24)
			{
				sprite.setRotation(48);
			}

			else if(infoX==21)
			{
				sprite.setRotation(42);
			}

			else if(infoX==18)
			{
				sprite.setRotation(36);
			}

			else if(infoX==15)
			{
				sprite.setRotation(30);
			}

			else if(infoX==12)
			{
				sprite.setRotation(24);
			}

			else if(infoX==9)
			{
				sprite.setRotation(6);
			}

			else if(infoX==6)
			{
				sprite.setRotation(0);
			}

			infoX--;
		}

		if(infoX!=0 && state==4)
		{
			moveX = moveX - (Gdx.graphics.getWidth()/3.08f)/25.0f;

			if(infoX==1)
			{
				moveX=-Gdx.graphics.getWidth()/3.08f;
			}

			if(infoX==24)
			{
				sprite.setRotation(48);
			}

			else if(infoX==21)
			{
				sprite.setRotation(42);
			}

			else if(infoX==18)
			{
				sprite.setRotation(36);
			}

			else if(infoX==15)
			{
				sprite.setRotation(30);
			}

			else if(infoX==12)
			{
				sprite.setRotation(24);
			}

			else if(infoX==9)
			{
				sprite.setRotation(6);
			}

			else if(infoX==6)
			{
				sprite.setRotation(0);
			}

			infoX--;
		}

		if(gameState == 0)
		{
			font2.draw(batch,"Tap to Play !",Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/5.4f,Gdx.graphics.getHeight()/2);

			if(Gdx.input.justTouched())
			{
				gameState=1;
			}
		}

		else if(gameState == 1)
		{
			driveSound.play();

			enemyVelocity = enemyVelocity + 0.0017f;

			if(enemyY[scoredEnemy] < taxiY)
			{
				score++;

				if(scoredEnemy<numberOfEnemies-1)
				{
					scoredEnemy++;
				}

				else
				{
					scoredEnemy=0;
				}
			}

			for(int i=0;i<numberOfEnemies;i++)
			{
				if(enemyY[i]<taxiY-Gdx.graphics.getHeight()/12)
				{
					enemyY[i] = enemyY[i] + numberOfEnemies*distance;
				}

				else
				{
					enemyY[i] = enemyY[i] - enemyVelocity;
				}

				if(i==0)
				{
					batch.draw(yellowcar,enemyX[i]-Gdx.graphics.getWidth()/3.08f,enemyY[i],Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/15);
					batch.draw(greencar,enemyX[i],enemyY[i],Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/15);
					batch.draw(bluecar,enemyX[i]+Gdx.graphics.getWidth()/3.08f,enemyY[i],Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/15);

					enemyCircles[i] = new Circle(enemyX[i]-Gdx.graphics.getWidth()/5.14f,enemyY[i]+Gdx.graphics.getHeight()/30.85f,Gdx.graphics.getWidth()/30);
					enemyCircles2[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/8,enemyY[i]+Gdx.graphics.getHeight()/30.85f,Gdx.graphics.getWidth()/30);
					enemyCircles3[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/2.2f,enemyY[i]+Gdx.graphics.getHeight()/30.85f,Gdx.graphics.getWidth()/30);
				}

				if(i==1)
				{
					batch.draw(yellowcar,enemyX[i]-Gdx.graphics.getWidth()/3.08f,enemyY[i],Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/15);
					batch.draw(greencar,enemyX[i],enemyY[i],Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/15);
					//batch.draw(bluecar,enemyX[i]+350,enemyY[i],Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/15);

					enemyCircles[i] = new Circle(enemyX[i]-Gdx.graphics.getWidth()/5.14f,enemyY[i]+Gdx.graphics.getHeight()/30.85f,Gdx.graphics.getWidth()/30);
					enemyCircles2[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/8,enemyY[i]+Gdx.graphics.getHeight()/30.85f,Gdx.graphics.getWidth()/30);
					//enemyCircles3[i] = new Circle(enemyX[i]+490,enemyY[i]+70,Gdx.graphics.getWidth()/17);
				}

				if(i==2)
				{
					batch.draw(yellowcar,enemyX[i]-Gdx.graphics.getWidth()/3.08f,enemyY[i],Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/15);
					//batch.draw(greencar,enemyX[i],enemyY[i],Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/15);
					batch.draw(bluecar,enemyX[i]+Gdx.graphics.getWidth()/3.08f,enemyY[i],Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/15);

					enemyCircles[i] = new Circle(enemyX[i]-Gdx.graphics.getWidth()/5.14f,enemyY[i]+Gdx.graphics.getHeight()/30.85f,Gdx.graphics.getWidth()/30);
					//enemyCircles2[i] = new Circle(enemyX[i]+135,enemyY[i]+70,Gdx.graphics.getWidth()/17);
					enemyCircles3[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/2.2f,enemyY[i]+Gdx.graphics.getHeight()/30.85f,Gdx.graphics.getWidth()/30);
				}

				if(i==3)
				{
					//batch.draw(yellowcar,enemyX[i]-350,enemyY[i],Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/15);
					batch.draw(greencar,enemyX[i],enemyY[i],Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/15);
					batch.draw(bluecar,enemyX[i]+Gdx.graphics.getWidth()/3.08f,enemyY[i],Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/15);

					//enemyCircles[i] = new Circle(enemyX[i]-210,enemyY[i]+70,Gdx.graphics.getWidth()/17);
					enemyCircles2[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/8,enemyY[i]+Gdx.graphics.getHeight()/30.85f,Gdx.graphics.getWidth()/30);
					enemyCircles3[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/2.2f,enemyY[i]+Gdx.graphics.getHeight()/30.85f,Gdx.graphics.getWidth()/30);
				}
			}

		}

		else if(gameState == 2)
		{
			font3.draw(batch,"Game Over ! Tap to Play Again !",Gdx.graphics.getWidth()/2-Gdx.graphics.getWidth()/2.7f,Gdx.graphics.getHeight()/2);

			if(Gdx.input.justTouched())
			{
				gameState=1;
				moveX=0;
				score=0;
				scoredEnemy=0;
				enemyVelocity=10;
				sprite.setRotation(0);
				state=0;

				for(int i=0;i<numberOfEnemies;i++)
				{
					enemyY[i] = Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/14.4f + i*distance;
					enemyX[i] = Gdx.graphics.getWidth()/3 + Gdx.graphics.getWidth()/15.4f;

					enemyCircles[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();
				}
			}
		}

		if(score>highScore)
		{
			highScore=score;
			prefs.putInteger("highscore",score);
			prefs.flush();
		}

		sScore = Integer.toString(score);
		font.draw(batch,"Score: " + sScore,Gdx.graphics.getWidth()/33,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/12);
		highScore = prefs.getInteger("highscore");
		sHighScore = Integer.toString(highScore);
		font.draw(batch,"High Score: " + sHighScore,Gdx.graphics.getWidth()/33,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/8);

		batch.end();

		taxiCircle.set(taxiX+Gdx.graphics.getWidth()/7.71f+moveX,taxiY+Gdx.graphics.getHeight()/58.37f+moveY,Gdx.graphics.getWidth()/30);

		/*
		if(state==1 || state==0)
		{
			taxiCircle.set(taxiX+Gdx.graphics.getWidth()/7.71f,taxiY+Gdx.graphics.getHeight()/58.37f+moveY,Gdx.graphics.getWidth()/30);
		}

		else if(state==2)
		{
			taxiCircle.set(taxiX+Gdx.graphics.getWidth()/7.71f+Gdx.graphics.getWidth()/3.08f,taxiY+Gdx.graphics.getHeight()/58.37f+moveY,Gdx.graphics.getWidth()/30);
		}

		else if(state==3)
		{
			taxiCircle.set(taxiX+Gdx.graphics.getWidth()/7.71f,taxiY+Gdx.graphics.getHeight()/58.37f+moveY,Gdx.graphics.getWidth()/30);
		}

		else if(state==4)
		{
			taxiCircle.set(taxiX+Gdx.graphics.getWidth()/7.71f-Gdx.graphics.getWidth()/3.08f,taxiY+Gdx.graphics.getHeight()/58.37f+moveY,Gdx.graphics.getWidth()/30);
		}*/

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLACK);
		//shapeRenderer.circle(taxiCircle.x,taxiCircle.y,taxiCircle.radius);
		//shapeRenderer.end();


		for(int i=0;i<numberOfEnemies;i++)
		{
			if((secondY>264 && enemyCircles[i].y<540) || (secondY>264 && enemyCircles2[i].y<540) || (secondY>264 && enemyCircles3[i].y<540))
			{
				gameState=1;
			}

			else if(enemyCircles[i].y<190 || enemyCircles2[i].y<190 || enemyCircles3[i].y<190)
			{
				if(Intersector.overlaps(taxiCircle,enemyCircles[i]) || Intersector.overlaps(taxiCircle,enemyCircles2[i]) || Intersector.overlaps(taxiCircle,enemyCircles3[i]))
				{
					gameState=2;
				}
			}
		}

	}

	@Override
	public void dispose ()
	{
		batch.dispose();
		font.dispose();
		jumpSound.dispose();
		driveSound.dispose();
		swipeSound.dispose();
	}

}
