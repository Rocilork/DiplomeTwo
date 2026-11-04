package com.example.dip

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object SB {
    val supabase = createSupabaseClient(
        supabaseUrl = "https://mxylzwxusgpacxxsvhnt.supabase.co",
        supabaseKey = "sb_publishable__4Cz-E5QJSqodfVwvAbcYw_FHHtqw25"
    ) {
        install(Auth)
        install(Postgrest)
        install(Realtime)
        //install other modules
    }
    fun getClient(): SupabaseClient{
        return supabase
    }
}