/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.lightjason.agentspeak.action.listsettuple;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.action.listsettuple.set.CAdd;
import org.lightjason.agentspeak.action.listsettuple.set.CContains;
import org.lightjason.agentspeak.action.listsettuple.set.CCreate;
import org.lightjason.agentspeak.action.listsettuple.set.CRemove;
import org.lightjason.agentspeak.action.listsettuple.set.CToList;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * collection set tests
 */
public final class TestCActionCollectionSet extends IBaseTest
{

    /**
     * test create
     */
    @Test
    public void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof Set<?> );
        Assert.assertArrayEquals( Stream.of( 1, 2 ).toArray(), l_return.get( 0 ).<Set<?>>raw().toArray() );
    }

    /**
     * test create synchronized
     */
    @Test
    public void createsynchronized()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            true, IContext.EMPTYPLAN,
            Stream.of( 10, 20 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( Collections.synchronizedSet( Collections.emptySet() ).getClass(), l_return.get( 0 ).raw().getClass() );
        Assert.assertArrayEquals( Stream.of( 20, 10 ).toArray(), l_return.get( 0 ).<Set<?>>raw().toArray() );
    }


    /**
     * test add
     */
    @Test
    public void add()
    {
        final Set<Object> l_set = new HashSet<>();

        new CAdd().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_set, 1, 5, 7 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertArrayEquals( Stream.of( 1, 5, 7 ).toArray(), l_set.toArray() );
    }

    /**
     * test contains
     */
    @Test
    public void contains()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Set<Object> l_set = Stream.of( "foo", 1, 2 ).collect( Collectors.toSet() );

        new CContains().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_set, "foo", 1, "bar" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals( Stream.of( true, true, false ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }

    /**
     * test remove
     */
    @Test
    public void remove()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Set<Object> l_set = Stream.of( "foo", 1, 2 ).collect( Collectors.toSet() );

        new CRemove().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_set, "foo", 1, 5 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( true, true, false ).toArray() );
        Assert.assertArrayEquals( Stream.of( 2 ).toArray(), l_set.toArray() );
    }

    /**
     * test to-list
     */
    @Test
    public void tolist()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Set<Object> l_set = Stream.of( "foobar", "x", "y" ).collect( Collectors.toSet() );

        new CToList().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_set ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assert.assertArrayEquals( l_set.toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
    }

}
