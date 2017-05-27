package com.feup.superslimefootball.controller.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.feup.superslimefootball.model.entities.EntityModel;

import static com.feup.superslimefootball.view.GameView.PPM;
import static com.feup.superslimefootball.view.GameView.VIEWPORT_HEIGHT;
import static com.feup.superslimefootball.view.GameView.VIEWPORT_WIDTH;

/**
 * Created by afonso on 5/27/17.
 */

public class WallsBody extends EntityBody {
    /**
     * Constructs a body representing a model in a certain world.
     *
     * @param world   The world this body lives on.
     * @param model   The model representing the body.
     */
    public WallsBody(World world, EntityModel model) {
        super(world, model, false);

        Vector2[] vertexes = new Vector2[5];
        vertexes[0] = new Vector2(0/PPM, 0);
        vertexes[1] = new Vector2(VIEWPORT_WIDTH / PPM, 0);
        vertexes[2] = new Vector2(VIEWPORT_WIDTH / PPM, VIEWPORT_HEIGHT/PPM);
        vertexes[3] = new Vector2(0, VIEWPORT_HEIGHT/PPM);
        vertexes[4] = new Vector2(0/PPM, 0);

        float density = 1.0f;

        createFixture(body, vertexes, density);
    }
}
