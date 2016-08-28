/*
 * Minecraft Forge
 * Copyright (c) 2016.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.feed_the_beast.ftbl.api.permissions.context;

import javax.annotation.Nullable;

public enum EmptyContext implements IContext
{
    INSTANCE;

    @Override
    public <T> T get(ContextKey<T> key)
    {
        return null;
    }

    @Override
    public boolean has(ContextKey<?> key)
    {
        return false;
    }

    @Override
    public <T> IContext set(ContextKey<T> key, @Nullable T obj)
    {
        return this;
    }
}