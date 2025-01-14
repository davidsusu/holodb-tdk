<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

use App\Models\Company;

class CompanySeeder extends Seeder
{
    public function run(): void
    {
        DB::statement('ALTER TABLE companies DISABLE KEYS');
        foreach (range(1, 2) as $batch) {
            Company::factory()->count(1000)->create();
        }
        DB::statement('ALTER TABLE companies ENABLE KEYS');
    }
}
