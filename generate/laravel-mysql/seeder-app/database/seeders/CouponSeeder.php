<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

use App\Models\Coupon;

class CouponSeeder extends Seeder
{
    public function run(): void
    {
        DB::statement('ALTER TABLE companies DISABLE KEYS');
        //foreach (range(1, 10000) as $batch) {
        foreach (range(1, 1) as $batch) {
            Coupon::factory()->count(1000)->create();
        }
        DB::statement('ALTER TABLE companies ENABLE KEYS');
    }
}
