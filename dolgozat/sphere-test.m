[x,y,z] = sphere;
figure
hs1 = surf(x,y,z);
hold on
hs2 = surf(x+3,y-2,z); % centered at (3,-2,0)
hs3 = surf(x,y+1,z-3); % centered at (0,1,-3)
q1 = get(hs1);
set(hs1, 'FaceColor', [1 0 0])
set(hs2, 'FaceColor', [0 1 0])
set(hs3, 'FaceColor', [0 0 1])
axis equal
