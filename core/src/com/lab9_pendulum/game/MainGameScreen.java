package com.lab9_pendulum.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;

public class MainGameScreen implements Screen {
    private World world;               // переменная для управления миром
    private Box2DDebugRenderer rend;   // отладочный отрисовщик тел Мира
    private OrthographicCamera camera; // видеокамера
    private Body rect;                 // тело прямоугольника
    private Body circle;               // тело окружности

    /*
    private int Nblock;
    private float Nf;
    private int flag;

    public MainGameScreen() {
        flag=1;
    }
     */

    @Override
    public void show() {
        // Создание нового мира – задан вектор гравитации в Мире
        world = new World(new Vector2(0,-10), true);

        // Создать камеру с охватом холста 20х15 метров
        camera = new OrthographicCamera(20,15);

        // Позиционировать камеру по центру холста
        camera.position.set(new Vector2(10,7.5f),0);

        // Обновление состояния камеры
        camera.update();

        // Создать отладочный отрисовщик
        rend = new Box2DDebugRenderer();

        // Создаем первый маятник
        circle = createCircle(BodyDef.BodyType.DynamicBody, new Vector2(9,5), 0.8f);
        rect = createRect(BodyDef.BodyType.KinematicBody, new Vector2(9, 12), new Vector2(1f,0.3f));
        createJoint(circle, rect);

        // Создаем второй маятник
        circle = createCircle(BodyDef.BodyType.DynamicBody, new Vector2(25,5), 0.8f);
        rect = createRect(BodyDef.BodyType.KinematicBody, new Vector2(11, 12), new Vector2(1f,0.3f));
        createJoint(circle, rect);

        // Вызвыть процедуру создания контуров внешних стен
        // createWall();
    }

    @Override
    public void render(float delta) {

        // Очистка экрана
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Отрисовка
        rend.render(world, camera.combined); // закомменировать после отладки

        // Выполнение расчета нового состояния Мира
        world.step(1 / 60f, 4, 4);

        /*
        if (Nblock < 100)
        {
            Nf += delta;
            if (Nf > 0.1f)
            {
                if (flag>0) createRect();
                else createCircle();
                flag*=-1;
                Nblock += 1;
                Nf = 0;
            }
        }
        */


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Удаление всех тел Мира
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        for(int i = 0; i < bodies.size; i++) world.destroyBody(bodies.get(i));

        rend.dispose();
        world.dispose();

    }

    // Функция создания крепления между объектами
    private Joint createJoint(Body circle, Body rect){
        RevoluteJointDef jDef = new RevoluteJointDef();
        jDef.bodyA = circle;
        jDef.bodyB = rect;
        jDef.collideConnected = true;
        jDef.localAnchorA.set(0, 7); // длина
        return world.createJoint(jDef);
    }

    // Функция создания тела прямоугольника
    private Body createRect(BodyDef.BodyType type, Vector2 position, Vector2 size) {

        // Структура геометрических свойств тела
        BodyDef bDef = new BodyDef();

        // Задать телу статический или динамический тип
        bDef.type = type;

        // Задать позицию тела в Мире – в метрах X и Y
        bDef.position.set(position.x, position.y);

        // Создание тела в Мире
        rect = world.createBody(bDef);

        // Создать эскиз контура тела в виде прямоугольника
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x, size.y);

        // Структура физических свойств тела
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;         // назначить вид контура тела
        fDef.density = 2;           // назначить плотность тела г/см3
        fDef.restitution = 0.7f;    // назначить упругость
        fDef.friction = 0.1f;       // назначить коэф-т трения
        rect.createFixture(fDef);   // закрепить свойства за телом

        return rect;
    }

    // Процедура создания тела окружности
    private Body createCircle(BodyDef.BodyType type, Vector2 position, float radius) {

        // Структура геометрических свойств тела
        BodyDef bDef = new BodyDef();

        // Задать телу статический или динамический тип
        bDef.type = type;

        // Задать позицию тела в Мире – в метрах X и Y
        bDef.position.set(position.x, position.y);

        // Создание тела в Мире
        circle = world.createBody(bDef);

        // Создать эскиз контура тела в виде окружности
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        // Структура физических свойств тела
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;         // назначить вид контура тела
        fDef.density = 2;           // назначить плотность тела г/см3
        fDef.restitution = 1f;      // назначить упругость
        fDef.friction = 0.1f;       // назначить коэф-т трения
        circle.createFixture(fDef); // закрепить свойства за телом

        return circle;
    }

    // Процедура создания тела прямоугольника (старая)
    private void createRectangle1(){

        // Структура геометрических свойств тела
        BodyDef bDef= new BodyDef();

        // задать телу тип динамического тела (на него действует гравитация)
        bDef.type= BodyDef.BodyType.DynamicBody;

        // задать позицию тела в Мире – в метрах X и Y
        // bDef.position.set(10,13);
        bDef.position.set((int)(Math.random()*10f+2f), 14);

        // создание тела в Мире
        rect=world.createBody(bDef);

        // Создать эскиз контура тела в виде прямоугольника
        PolygonShape shape = new PolygonShape();
        // shape.setAsBox(2,2);
        shape.setAsBox((float)(Math.random()+0.1f),(float)(Math.random()+0.1f));

        // Структура физических свойств тела
        FixtureDef fDef=new FixtureDef();
        fDef.shape=shape;         // назначить вид контура тела
        fDef.density=2;           // назначить плотность тела г/см3
        fDef.restitution=0.7f;    // назначить упругость
        fDef.friction=0.1f;       // назначить коэф-т трения
        rect.createFixture(fDef); // закрепить свойства за телом
    }

    // Процедура создания тела окружности (старая)
    private void createCircle1(){
        // Структура геометрических свойств тела
        BodyDef bDef= new BodyDef();

        // Задать телу тип динамического тела (на него действует гравитация)
        bDef.type= BodyDef.BodyType.DynamicBody;

        // Задать позицию тела в Мире – в метрах X и Y
        bDef.position.set((int)(Math.random()*10f+2f), 14);

        // Создание тела в Мире
        rect=world.createBody(bDef);

        // Создать эскиз контура тела в виде окружности радиусом 1,5 м
        CircleShape shape =new CircleShape();
        shape.setRadius((float)(Math.random()+0.1f));

        // Структура физических свойств тела
        FixtureDef fDef=new FixtureDef();
        fDef.shape=shape;         // назначить вид контура тела
        fDef.density=2;           // назначить плотность тела г/см3
        fDef.restitution=0.7f;    // назначить упругость
        fDef.friction=0.1f;       // назначить коэф-т трения
        rect.createFixture(fDef); // закрепить свойства за телом
    }

    // Процедура создания внешних стен
    private void createWall()
    {
        BodyDef bDef= new BodyDef();
        bDef.type= BodyDef.BodyType.StaticBody;
        bDef.position.set(0,0);

        Body w = world.createBody(bDef);
        ChainShape shape = new ChainShape();

        // Контур стены в виде перевернутой трапеции без основания
        shape.createChain(new Vector2[]{new Vector2(1,15),new Vector2(1,1),
                new Vector2(19,1),new Vector2(19,15)});

        FixtureDef fDef=new FixtureDef();
        fDef.shape=shape;
        fDef.friction=0.1f;
        w.createFixture(fDef);
    }


}
